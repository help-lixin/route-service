package help.lixin.route.core.loadbalancer;

import help.lixin.route.constants.Constants;
import help.lixin.route.core.parse.RouteParseServiceFace;
import help.lixin.route.core.serviceid.IServiceIdService;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;
import help.lixin.route.model.RouteInfoList;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.client.AbstractClientHttpRequest;

import java.lang.reflect.Field;
import java.util.List;

public class BlockingLoadBalancerClientExt extends BlockingLoadBalancerClient {
    private RouteParseServiceFace routeParseServiceFace;
    private IServiceIdService serviceIdService;


    public BlockingLoadBalancerClientExt(LoadBalancerClientFactory loadBalancerClientFactory, LoadBalancerProperties properties, RouteParseServiceFace routeParseServiceFace, IServiceIdService serviceIdService) {
        super(loadBalancerClientFactory);
        this.routeParseServiceFace = routeParseServiceFace;
        this.serviceIdService = serviceIdService;
    }

    public void setRouteParseServiceFace(RouteParseServiceFace routeParseServiceFace) {
        this.routeParseServiceFace = routeParseServiceFace;
    }

    public RouteParseServiceFace getRouteParseServiceFace() {
        return routeParseServiceFace;
    }

    public void setServiceIdService(IServiceIdService serviceIdService) {
        this.serviceIdService = serviceIdService;
    }

    public IServiceIdService getServiceIdService() {
        return serviceIdService;
    }

    @Override
    public <T> ServiceInstance choose(String serviceId, Request<T> request) {
        String newServiceId = null;
        if (request instanceof LoadBalancerRequestAdapter) { // ribbon的独特处理,不太建议用ribbon,里面需要反射
            newServiceId = processLoadBalancerRequestAdapter(serviceId, request);
        } else if (request instanceof DefaultRequest) {
            newServiceId = processDefaultRequest(serviceId, request);
        }

        if (null == newServiceId) {
            newServiceId = serviceId;
        }

        return super.choose(newServiceId, request);
    }


    public String processLoadBalancerRequestAdapter(String serviceId, Request request) {
        LoadBalancerRequestAdapter<LoadBalancerRequest<DefaultRequest>, DefaultRequestContext> loadBalancerRequestAdapter = (LoadBalancerRequestAdapter<LoadBalancerRequest<DefaultRequest>, DefaultRequestContext>) request;
        DefaultRequestContext context = loadBalancerRequestAdapter.getContext();
        Object clientRequest = context.getClientRequest();
        try {
            Field[] fields = clientRequest.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(Boolean.TRUE);
                Object object = field.get(clientRequest);
                if (object instanceof AbstractClientHttpRequest) { // 处理这段逻辑后,就跳出循环,不需要再继续往下去比较了.
                    AbstractClientHttpRequest clientHttpRequest = (AbstractClientHttpRequest) object;
                    // 从请求头里,获得x-route信息
                    List<String> routeHeaderList = clientHttpRequest.getHeaders().get(Constants.ROUTE_KEY);
                    if (null != routeHeaderList && routeHeaderList.size() > 0) {
                        String xroute = routeHeaderList.get(0);
                        // 判断serviceId是否在x-route协议头里有配置,如果有配置,代表要做特殊处理.
                        RouteInfoList routeInfoList = routeParseServiceFace.transform(xroute);
                        if (null != routeInfoList) {
                            IRouteInfo routeInfo = routeInfoList.getRouteInfos().get(serviceId);
                            if (null != routeInfo && routeInfo instanceof RouteInfo) {
                                RouteInfo tmpRouteInfo = (RouteInfo) routeInfo;
                                // group#serviceId#host#port
                                String newServiceId = serviceIdService.encode(serviceId, routeInfo);
                                return newServiceId;
                            }
                        }
                    }
                    break;
                }
            }
        } catch (Exception ignore) {
        }
        return null;
    }

    public String processDefaultRequest(String serviceId, Request request) {
        // 从请求头里,获得x-route信息
        DefaultRequest<RequestDataContext> defaultRequest = (DefaultRequest<RequestDataContext>) request;

        RequestData clientRequest = defaultRequest.getContext().getClientRequest();
        List<String> routeHeaderList = clientRequest.getHeaders().get(Constants.ROUTE_KEY);

        if (null != routeHeaderList && routeHeaderList.size() > 0) {
            String xroute = routeHeaderList.get(0);
            // 判断serviceId是否在x-route协议头里有配置,如果有配置,代表要做特殊处理.
            RouteInfoList routeInfoList = routeParseServiceFace.transform(xroute);
            if (null != routeInfoList) {
                IRouteInfo routeInfo = routeInfoList.getRouteInfos().get(serviceId);
                if (null != routeInfo && routeInfo instanceof RouteInfo) {
                    RouteInfo tmpRouteInfo = (RouteInfo) routeInfo;
                    // group#serviceId#host#port
                    String newServiceId = serviceIdService.encode(serviceId, routeInfo);
                    return newServiceId;
                }
            }
        }
        return null;
    }
}
