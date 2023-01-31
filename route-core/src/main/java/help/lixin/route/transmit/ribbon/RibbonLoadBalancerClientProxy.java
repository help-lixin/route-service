package help.lixin.route.transmit.ribbon;

import java.io.IOException;
import java.net.URI;

import help.lixin.route.core.context.RouteInfoHolder;
import help.lixin.route.core.meta.RouteServiceFace;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.model.IRouteInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;


@Deprecated
public class RibbonLoadBalancerClientProxy implements LoadBalancerClient {

    private Logger logger = LoggerFactory.getLogger(RibbonLoadBalancerClientProxy.class);

    private RibbonLoadBalancerClient ribbonLoadBalancerClient;

    private RouteServiceFace routeServiceFace;

    public void setRouteServiceFace(RouteServiceFace routeServiceFace) {
        this.routeServiceFace = routeServiceFace;
    }

    public RouteServiceFace getRouteServiceFace() {
        return routeServiceFace;
    }

    public void setProxyTarget(RibbonLoadBalancerClient ribbonLoadBalancerClient) {
        this.ribbonLoadBalancerClient = ribbonLoadBalancerClient;
    }

    public RibbonLoadBalancerClient getProxyTarget() {
        return ribbonLoadBalancerClient;
    }

    public void setRibbonLoadBalancerClient(RibbonLoadBalancerClient ribbonLoadBalancerClient) {
        this.ribbonLoadBalancerClient = ribbonLoadBalancerClient;
    }

    public RibbonLoadBalancerClient getRibbonLoadBalancerClient() {
        return ribbonLoadBalancerClient;
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        return ribbonLoadBalancerClient.choose(serviceId);
    }

    @Override
    public <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException {
        // 1. RouteFilter会过滤请求,把协议头里的内容,转换成业务模型.
        if (RouteInfoHolder.isEnabled()) {
            IRouteInfo routeInfo = RouteInfoHolder.get().getRouteInfos().get(serviceId);
            if (null != routeInfo) {
                if (logger.isDebugEnabled()) {
                    logger.debug("proxy micro service name [{}] to rule [{}]", serviceId, routeInfo);
                }
                // 构建:RouteInfoContext
                RouteInfoContext ctx = RouteInfoContext.newBuilder().routeInfo(routeInfo).build();
                ServiceInstance serviceInstance = routeServiceFace.getServiceInstance(ctx);
                if (null != serviceInstance) {
                    return this.execute(serviceId, serviceInstance, request);
                }
            }
        }
        // 委托给代理类处理.
        return ribbonLoadBalancerClient.execute(serviceId, request);
    }

    @Override
    public <T> T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest<T> request)
            throws IOException {
        return ribbonLoadBalancerClient.execute(serviceId, serviceInstance, request);
    }

    @Override
    public URI reconstructURI(ServiceInstance instance, URI original) {
        return ribbonLoadBalancerClient.reconstructURI(instance, original);
    }
}