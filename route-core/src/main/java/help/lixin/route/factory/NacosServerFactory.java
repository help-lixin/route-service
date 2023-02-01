package help.lixin.route.factory;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.loadbalancer.Server;
import help.lixin.route.filter.IServerFactory;
import help.lixin.route.model.RouteInfo;

public class NacosServerFactory implements IServerFactory {
    @Override
    public Server create(RouteInfo routeInfo) {
        Instance instance = new Instance();
        instance.setIp(routeInfo.getIp());
        instance.setPort(routeInfo.getPort());
        instance.setServiceName(routeInfo.getServiceId());
        instance.setInstanceId(routeInfo.getServiceId());

        NacosServer server = new NacosServer(instance);
        return server;
    }
}
