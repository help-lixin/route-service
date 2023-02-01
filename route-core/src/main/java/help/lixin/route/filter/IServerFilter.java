package help.lixin.route.filter;

import com.netflix.loadbalancer.Server;
import help.lixin.route.core.meta.ctx.RouteInfoContext;

import java.util.List;

public interface IServerFilter<T extends Server> {
    void filter(RouteInfoContext ctx, List<T> instances);
}
