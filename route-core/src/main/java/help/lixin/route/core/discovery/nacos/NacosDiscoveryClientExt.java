package help.lixin.route.core.discovery.nacos;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.discovery.DiscoveryClientTemplate;
import help.lixin.route.core.discovery.IDiscoveryClientTemplate;
import help.lixin.route.core.discovery.ServiceInstanceCallback;
import help.lixin.route.core.discovery.polaris.PolarisDiscoveryClientExt;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import help.lixin.route.model.RouteInfo;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.List;

public class NacosDiscoveryClientExt extends NacosDiscoveryClient {
    private IDiscoveryClientTemplate discoveryClientTemplate;

    public NacosDiscoveryClientExt(NacosServiceDiscovery nacosServiceDiscovery, IDiscoveryClientTemplate discoveryClientTemplate) {
        super(nacosServiceDiscovery);
        this.discoveryClientTemplate = discoveryClientTemplate;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceIdCtx) {
        return discoveryClientTemplate.getInstances(serviceIdCtx, new ServiceInstanceCallback() {
            @Override
            public List<ServiceInstance> getInstanceList(String serviceId) {
                return NacosDiscoveryClientExt.super.getInstances(serviceId);
            }
        });
    }
}