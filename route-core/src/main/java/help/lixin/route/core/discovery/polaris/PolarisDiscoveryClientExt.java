package help.lixin.route.core.discovery.polaris;

import com.tencent.cloud.polaris.discovery.PolarisDiscoveryClient;
import com.tencent.cloud.polaris.discovery.PolarisServiceDiscovery;
import help.lixin.route.core.discovery.DiscoveryClientTemplate;
import help.lixin.route.core.discovery.IDiscoveryClientTemplate;
import help.lixin.route.core.discovery.ServiceInstanceCallback;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public class PolarisDiscoveryClientExt extends PolarisDiscoveryClient {
    private IDiscoveryClientTemplate discoveryClientTemplate;

    public PolarisDiscoveryClientExt(PolarisServiceDiscovery polarisServiceDiscovery, IDiscoveryClientTemplate discoveryClientTemplate) {
        super(polarisServiceDiscovery);
        this.discoveryClientTemplate = discoveryClientTemplate;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceIdCtx) {
        return discoveryClientTemplate.getInstances(serviceIdCtx, new ServiceInstanceCallback() {
            @Override
            public List<ServiceInstance> getInstanceList(String serviceId) {
                return PolarisDiscoveryClientExt.super.getInstances(serviceId);
            }
        });
    }
}
