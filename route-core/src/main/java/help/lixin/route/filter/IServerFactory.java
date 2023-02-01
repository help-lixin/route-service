package help.lixin.route.filter;

import com.netflix.loadbalancer.Server;
import help.lixin.route.model.RouteInfo;

public interface IServerFactory {
    Server create(RouteInfo routeInfo);
}
