package help.lixin.route.transmit.ribbon;

import java.io.IOException;
import java.net.URI;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import help.lixin.route.core.context.RouteInfoHolder;
import help.lixin.route.core.meta.RouteServiceFace;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.ribbon.ZoneAwareLoadBalancerExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;


public class RibbonLoadBalancerClientProxy extends RibbonLoadBalancerClient implements LoadBalancerClient {
    private Logger logger = LoggerFactory.getLogger(RibbonLoadBalancerClientProxy.class);

//    private RibbonLoadBalancerClient ribbonLoadBalancerClient;

    private RouteServiceFace routeServiceFace;

    public RibbonLoadBalancerClientProxy(SpringClientFactory clientFactory) {
        super(clientFactory);
    }

    public void setRouteServiceFace(RouteServiceFace routeServiceFace) {
        this.routeServiceFace = routeServiceFace;
    }

    public RouteServiceFace getRouteServiceFace() {
        return routeServiceFace;
    }

    protected Server getServer(ILoadBalancer loadBalancer, Object hint) {
        if (loadBalancer == null) {
            return null;
        }

        if (loadBalancer instanceof ZoneAwareLoadBalancerExt) {
            ZoneAwareLoadBalancerExt zoneAwareLoadBalancerExt = (ZoneAwareLoadBalancerExt) loadBalancer;
            // 1. RouteFilter会过滤请求,把协议头里的内容,转换成业务模型.
//            if (RouteInfoHolder.isEnabled()) {
//                IRouteInfo routeInfo = RouteInfoHolder.get().getRouteInfos().get(serviceId);
//                if (null != routeInfo) {
//                    if (logger.isDebugEnabled()) {
//                        logger.debug("proxy micro service name [{}] to rule [{}]", serviceId, routeInfo);
//                    }
//                    // 构建:RouteInfoContext
//                    RouteInfoContext ctx = RouteInfoContext.newBuilder().routeInfo(routeInfo).build();
//                    ServiceInstance serviceInstance = routeServiceFace.getServiceInstance(ctx);
//                    if (null != serviceInstance) {
//                        return this.execute(serviceId, serviceInstance, request);
//                    }
//                }
//            }
        }
        return null;
    }
}