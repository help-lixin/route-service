package help.lixin.route.core.discovery;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface IDiscoveryClientTemplate {
    List<ServiceInstance> getInstances(String serviceIdCtx, ServiceInstanceCallback callback);
}
