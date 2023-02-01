package help.lixin.route.ribbon;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.cloud.nacos.ribbon.NacosServerList;
import com.netflix.loadbalancer.Server;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.context.RouteInfoHolder;
import help.lixin.route.core.context.XRouteHolder;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServerFilterFace;
import help.lixin.route.model.IRouteInfo;

import java.util.ArrayList;
import java.util.List;

public class NacosServerListExt extends NacosServerList {

    private IServerFilterFace<Server> serverFilterFace;

    public NacosServerListExt(NacosDiscoveryProperties discoveryProperties) {
        super(discoveryProperties);
    }

    public void setServerFilterFace(IServerFilterFace<Server> serverFilterFace) {
        this.serverFilterFace = serverFilterFace;
    }

    public IServerFilterFace<Server> getServerFilterFace() {
        return serverFilterFace;
    }

    @Override
    public List<NacosServer> getInitialListOfServers() {
        return this.getUpdatedListOfServers();
    }

    @Override
    public List<NacosServer> getUpdatedListOfServers() {
        List<NacosServer> updatedListOfServers = super.getUpdatedListOfServers();
        // 临时结果集
        List instances = new ArrayList<>();
        instances.addAll(updatedListOfServers);

        if (null != serverFilterFace && XRouteHolder.isEnabled()) {
            String serviceId = getServiceId();

            IRouteInfo routeInfo = RouteInfoHolder.get().getRouteInfos().get(serviceId);
            RouteInfoContext ctx = RouteInfoContext.newBuilder()
                    //
                    .routeInfo(routeInfo)
                    //
                    .other("serviceId", serviceId)
                    //
                    .other(Constants.DISCOVERY_TYPE, Constants.DISCOVERY_NACOS) // eureka/nacos
                    //
                    .build();
            serverFilterFace.filter(ctx, instances);
        }

        return instances;
    }
}
