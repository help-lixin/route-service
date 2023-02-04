package help.lixin.route.core.discovery;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface ServiceInstanceCallback {
    List<ServiceInstance> getInstanceList(String serviceId);
}
