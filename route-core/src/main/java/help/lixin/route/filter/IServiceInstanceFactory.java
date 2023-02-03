package help.lixin.route.filter;

import help.lixin.route.model.RouteInfo;
import org.springframework.cloud.client.ServiceInstance;

public interface IServiceInstanceFactory {
    ServiceInstance create(RouteInfo routeInfo);
}
