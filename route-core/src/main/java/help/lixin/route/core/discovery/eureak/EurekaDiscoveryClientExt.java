package help.lixin.route.core.discovery.eureak;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.discovery.DiscoveryClientTemplate;
import help.lixin.route.core.discovery.IDiscoveryClientTemplate;
import help.lixin.route.core.discovery.ServiceInstanceCallback;
import help.lixin.route.core.discovery.polaris.PolarisDiscoveryClientExt;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import help.lixin.route.model.RouteInfo;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;

import java.util.ArrayList;
import java.util.List;

public class EurekaDiscoveryClientExt extends EurekaDiscoveryClient {

    private IDiscoveryClientTemplate discoveryClientTemplate;

    public EurekaDiscoveryClientExt(EurekaClient eurekaClient, EurekaClientConfig clientConfig, IDiscoveryClientTemplate discoveryClientTemplate) {
        super(eurekaClient, clientConfig);
        this.discoveryClientTemplate = discoveryClientTemplate;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceIdCtx) {
        return discoveryClientTemplate.getInstances(serviceIdCtx, new ServiceInstanceCallback() {
            @Override
            public List<ServiceInstance> getInstanceList(String serviceId) {
                return EurekaDiscoveryClientExt.super.getInstances(serviceId);
            }
        });
    }
}
