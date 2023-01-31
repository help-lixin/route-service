package help.lixin.route.eureka;

import help.lixin.route.filter.IInstanceFilter;
import help.lixin.route.core.meta.ctx.RouteInfoContext;

import java.util.List;

/**
 * 对InstanceInfo进行过滤
 */
public interface IEurekaInstanceFilter<InstanceInfo> extends IInstanceFilter<InstanceInfo> {
    @Override
    void filter(RouteInfoContext ctx, List<InstanceInfo> instances);
}
