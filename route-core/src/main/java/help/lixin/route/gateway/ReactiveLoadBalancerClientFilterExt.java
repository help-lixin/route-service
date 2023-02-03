package help.lixin.route.gateway;

import help.lixin.route.constants.Constants;
import help.lixin.route.core.parse.RouteParseServiceFace;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;
import help.lixin.route.model.RouteInfoList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_LOADBALANCER_RESPONSE_ATTR;

public class ReactiveLoadBalancerClientFilterExt extends ReactiveLoadBalancerClientFilter {
    private static final Log log = LogFactory.getLog(ReactiveLoadBalancerClientFilter.class);

    private RouteParseServiceFace routeParseServiceFace;

    private LoadBalancerClientFactory clientFactory;
    private GatewayLoadBalancerProperties properties;
    private LoadBalancerProperties loadBalancerProperties;

    public ReactiveLoadBalancerClientFilterExt(LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties, LoadBalancerProperties loadBalancerProperties, RouteParseServiceFace routeParseServiceFace) {
        super(clientFactory, properties, loadBalancerProperties);
        this.clientFactory = clientFactory;
        this.properties = properties;
        this.loadBalancerProperties = loadBalancerProperties;

        this.routeParseServiceFace = routeParseServiceFace;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
        if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
            return chain.filter(exchange);
        }
        // preserve the original url
        addOriginalRequestUrl(exchange, url);

        if (log.isTraceEnabled()) {
            log.trace(ReactiveLoadBalancerClientFilter.class.getSimpleName() + " url before: " + url);
        }
        URI requestUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String serviceId = requestUri.getHost();

        List<String> routeHeaderList = exchange.getRequest().getHeaders().get(Constants.ROUTE_KEY);
        if (null != routeHeaderList && routeHeaderList.size() > 0) {
            String xroute = routeHeaderList.get(0);
            // 判断serviceId是否在x-route协议头里有配置,如果有配置,代表要做特殊处理.
            RouteInfoList routeInfoList = routeParseServiceFace.transform(xroute);
            if (null != routeInfoList) {
                IRouteInfo routeInfo = routeInfoList.getRouteInfos().get(serviceId);
                if (null != routeInfo && routeInfo instanceof RouteInfo) {
                    RouteInfo tmpRouteInfo = (RouteInfo) routeInfo;
                    // ***********************************************************************************
                    // 通过改变:serviceId来实现上下文的传递
                    // ***********************************************************************************
                    // serviceId#host#port
                    String newServiceId = String.format(Constants.SERVICE_ID_FORMAT, serviceId, tmpRouteInfo.getIp(), tmpRouteInfo.getPort());
                    serviceId = newServiceId;
                }
            }
        }

        Set<LoadBalancerLifecycle> supportedLifecycleProcessors = LoadBalancerLifecycleValidator
                .getSupportedLifecycleProcessors(clientFactory.getInstances(serviceId, LoadBalancerLifecycle.class),
                        RequestDataContext.class, ResponseData.class, ServiceInstance.class);
        DefaultRequest<RequestDataContext> lbRequest = new DefaultRequest<>(new RequestDataContext(
                new RequestData(exchange.getRequest()), getHint(serviceId, loadBalancerProperties.getHint())));
        return choose(lbRequest, serviceId, supportedLifecycleProcessors).doOnNext(response -> {

                    if (!response.hasServer()) {
                        supportedLifecycleProcessors.forEach(lifecycle -> lifecycle
                                .onComplete(new CompletionContext<>(CompletionContext.Status.DISCARD, lbRequest, response)));
                        throw NotFoundException.create(properties.isUse404(), "Unable to find instance for " + url.getHost());
                    }

                    ServiceInstance retrievedInstance = response.getServer();

                    URI uri = exchange.getRequest().getURI();

                    // if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
                    // if the loadbalancer doesn't provide one.
                    String overrideScheme = retrievedInstance.isSecure() ? "https" : "http";
                    if (schemePrefix != null) {
                        overrideScheme = url.getScheme();
                    }

                    DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(retrievedInstance,
                            overrideScheme);

                    URI requestUrl = reconstructURI(serviceInstance, uri);

                    if (log.isTraceEnabled()) {
                        log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
                    }
                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
                    exchange.getAttributes().put(GATEWAY_LOADBALANCER_RESPONSE_ATTR, response);
                    supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onStartRequest(lbRequest, response));
                }).then(chain.filter(exchange))
                .doOnError(throwable -> supportedLifecycleProcessors.forEach(lifecycle -> lifecycle
                        .onComplete(new CompletionContext<ResponseData, ServiceInstance, RequestDataContext>(
                                CompletionContext.Status.FAILED, throwable, lbRequest,
                                exchange.getAttribute(GATEWAY_LOADBALANCER_RESPONSE_ATTR)))))
                .doOnSuccess(aVoid -> supportedLifecycleProcessors.forEach(lifecycle -> lifecycle
                        .onComplete(new CompletionContext<ResponseData, ServiceInstance, RequestDataContext>(
                                CompletionContext.Status.SUCCESS, lbRequest,
                                exchange.getAttribute(GATEWAY_LOADBALANCER_RESPONSE_ATTR),
                                new ResponseData(exchange.getResponse(), new RequestData(exchange.getRequest()))))));
    }

    private Mono<Response<ServiceInstance>> choose(Request<RequestDataContext> lbRequest, String serviceId,
                                                   Set<LoadBalancerLifecycle> supportedLifecycleProcessors) {
        ReactorLoadBalancer<ServiceInstance> loadBalancer = this.clientFactory.getInstance(serviceId,
                ReactorServiceInstanceLoadBalancer.class);
        if (loadBalancer == null) {
            throw new NotFoundException("No loadbalancer available for " + serviceId);
        }
        supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onStart(lbRequest));
        return loadBalancer.choose(lbRequest);
    }

    private String getHint(String serviceId, Map<String, String> hints) {
        String defaultHint = hints.getOrDefault("default", "default");
        String hintPropertyValue = hints.get(serviceId);
        return hintPropertyValue != null ? hintPropertyValue : defaultHint;
    }
}
