package help.lixin.route.filter.impl;

import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServiceInstanceFactory;
import help.lixin.route.filter.IServiceInstanceFilter;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;
import org.springframework.beans.BeansException;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

public class RewriteEurekaRouteFilter implements IServiceInstanceFilter<ServiceInstance> {
    @Override
    public void filter(RouteInfoContext ctx, List<ServiceInstance> instances) {
        if (null != ctx) {
            IRouteInfo routeInfo = ctx.getRouteInfo();
            if (routeInfo instanceof RouteInfo) {
                RouteInfo routeInfoImpl = (RouteInfo) routeInfo;
                // 1. mock ServiceInstance
                ServiceInstance mockServiceInstance = new DefaultServiceInstance(routeInfoImpl.getServiceId(), routeInfoImpl.getServiceId(), routeInfoImpl.getIp(), routeInfoImpl.getPort(), false);

                // 2. 清空
                instances.clear();

                // 3. 重新添加
                instances.add(mockServiceInstance);
            }
        }
    }
}
