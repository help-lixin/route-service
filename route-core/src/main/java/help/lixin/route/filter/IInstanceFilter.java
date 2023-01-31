package help.lixin.route.filter;

import help.lixin.route.core.meta.ctx.RouteInfoContext;

import java.util.List;

public interface IInstanceFilter<T> {
    void filter(RouteInfoContext ctx, List<T> instances);
}
