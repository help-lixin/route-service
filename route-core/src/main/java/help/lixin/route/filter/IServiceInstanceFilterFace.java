package help.lixin.route.filter;

import help.lixin.route.core.meta.ctx.RouteInfoContext;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface IServiceInstanceFilterFace<T extends ServiceInstance> {
    void filter(RouteInfoContext ctx, List<T> instances);
}
