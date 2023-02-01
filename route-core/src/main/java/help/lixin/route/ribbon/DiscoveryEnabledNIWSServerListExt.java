package help.lixin.route.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.discovery.EurekaClient;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.context.RouteInfoHolder;
import help.lixin.route.core.context.XRouteHolder;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServerFilterFace;
import help.lixin.route.model.IRouteInfo;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class DiscoveryEnabledNIWSServerListExt extends DiscoveryEnabledNIWSServerList {

    private IServerFilterFace serverFilterFace;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public DiscoveryEnabledNIWSServerListExt() {
    }

    public DiscoveryEnabledNIWSServerListExt(String vipAddresses) {
        super(vipAddresses);
    }

    public DiscoveryEnabledNIWSServerListExt(IClientConfig clientConfig) {
        super(clientConfig);
    }

    public DiscoveryEnabledNIWSServerListExt(String vipAddresses, Provider<EurekaClient> eurekaClientProvider) {
        super(vipAddresses, eurekaClientProvider);
    }

    public DiscoveryEnabledNIWSServerListExt(IClientConfig clientConfig, Provider<EurekaClient> eurekaClientProvider) {
        super(clientConfig, eurekaClientProvider);
    }

    public void setServerFilterFace(IServerFilterFace serverFilterFace) {
        this.serverFilterFace = serverFilterFace;
    }

    public IServerFilterFace getServerFilterFace() {
        return serverFilterFace;
    }

    @Override
    public List<DiscoveryEnabledServer> getInitialListOfServers() {
        return getUpdatedListOfServers();
    }

    @Override
    public List<DiscoveryEnabledServer> getUpdatedListOfServers() {
        List<DiscoveryEnabledServer> updatedListOfServers = super.getUpdatedListOfServers();
        // 临时结果集
        List instances = new ArrayList<>();
        instances.addAll(updatedListOfServers);

        if (null != serverFilterFace && XRouteHolder.isEnabled()) {
            String serviceId = getVipAddresses();

            IRouteInfo routeInfo = RouteInfoHolder.get().getRouteInfos().get(serviceId);
            RouteInfoContext ctx = RouteInfoContext.newBuilder()
                    //
                    .routeInfo(routeInfo)
                    //
                    .other("serviceId", serviceId)
                    //
                    .other(Constants.DISCOVERY_TYPE, getType()) // eureka/nacos
                    //
                    .build();
            serverFilterFace.filter(ctx, instances);
        }
        return instances;
    }
}
