package help.lixin.route.meta.impl;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient.RibbonServer;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import help.lixin.route.meta.IRouteService;
import help.lixin.route.meta.ctx.RouteInfoContext;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * serviceId: hello-service <br/>
 * address: 192.168.100.9:8080 <br/>
 * 根据上面的参数,模拟出一个服务实例(ServiceInstance).
 *
 * @author lixin
 */
public class RewriteRouteService implements IRouteService {
    private Logger logger = LoggerFactory.getLogger(RewriteRouteService.class);

    @Override
    public ServiceInstance getServiceInstance(RouteInfoContext ctx) {
        IRouteInfo routeInfo = ctx.getRouteInfo();
        if (routeInfo instanceof RouteInfo) {
            String serviceId = routeInfo.getServiceId();
            RouteInfo rewriteRouteInfo = (RouteInfo) routeInfo;

            // 模拟出一个ServiceInstance的实现类
            InstanceInfo instanceInfo = InstanceInfo.Builder.newBuilder().setAppName(routeInfo.getServiceId()).build();
            Server server = new DiscoveryEnabledServer(instanceInfo, false);
            server.setHost(rewriteRouteInfo.getIp());
            server.setPort(rewriteRouteInfo.getPort());
            server.setSchemea("http");
            RibbonServer ribbonServer = new RibbonServer(serviceId, server, false, null);
            if (logger.isDebugEnabled()) {
                logger.debug("proxy micro service name [{}] to rule [{}]", serviceId, routeInfo);
            }
            return ribbonServer;
        }
        return null;
    }

    @Override
    public Class<?> supportType() {
        return RouteInfo.class;
    }
}
