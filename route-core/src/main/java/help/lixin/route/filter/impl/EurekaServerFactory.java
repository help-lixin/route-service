package help.lixin.route.filter.impl;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import help.lixin.route.filter.IServerFactory;
import help.lixin.route.model.RouteInfo;

public class EurekaServerFactory implements IServerFactory {
    @Override
    public Server create(RouteInfo routeInfo) {
        InstanceInfo instanceInfo = InstanceInfo.Builder.newBuilder()
                //
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
