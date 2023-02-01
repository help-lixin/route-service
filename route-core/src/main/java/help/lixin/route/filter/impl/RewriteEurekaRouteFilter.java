package help.lixin.route.filter.impl;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServerFilter;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;

import java.util.List;

public class RewriteEurekaRouteFilter implements IServerFilter<Server> {
    @Override
    public void filter(RouteInfoContext ctx, List<Server> instances) {
        // mock出一个
        IRouteInfo routeInfo = ctx.getRouteInfo();
        if (routeInfo instanceof RouteInfo) {
            RouteInfo routeInfoImpl = (RouteInfo) routeInfo;
            // 1. mock Server
            // 做成工厂吧
            String discoveryType = (String) ctx.getOthers().get(Constants.DISCOVERY_TYPE);
            Server mockServer = null;
            if (Constants.DISCOVERY_EUREKA.equals(discoveryType)) {
                mockServer = mockDiscoveryEnabledServer(routeInfoImpl);
            } else if (Constants.DISCOVERY_TYPE.equals(discoveryType)) {
                // TODO lixin
            }
            // 2. 清空
            instances.clear();

            // 3. 重新添加
            instances.add(mockServer);
        }
    }

    protected DiscoveryEnabledServer mockDiscoveryEnabledServer(RouteInfo routeInfo) {
        InstanceInfo instanceInfo = InstanceInfo.Builder.newBuilder()
                .setAppName(routeInfo.getServiceId())
                //
                .setVIPAddress(routeInfo.getIp())
                //
                .setHostName(routeInfo.getIp())
                //
                .setIPAddr(routeInfo.getIp())
                //
                .setPort(routeInfo.getPort())
                //
                .build();
        DiscoveryEnabledServer discoveryEnabledServer = new DiscoveryEnabledServer(instanceInfo, false);
        return discoveryEnabledServer;
    }
}
