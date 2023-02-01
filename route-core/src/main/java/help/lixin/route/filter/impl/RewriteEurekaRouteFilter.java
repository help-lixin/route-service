package help.lixin.route.filter.impl;

import com.netflix.loadbalancer.Server;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServerFactory;
import help.lixin.route.filter.IServerFilter;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

public class RewriteEurekaRouteFilter implements IServerFilter<Server>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void filter(RouteInfoContext ctx, List<Server> instances) {
        // mock出一个
        IRouteInfo routeInfo = ctx.getRouteInfo();
        if (routeInfo instanceof RouteInfo) {
            RouteInfo routeInfoImpl = (RouteInfo) routeInfo;

            IServerFactory serverFactory = applicationContext.getBean(IServerFactory.class);
            if (null != serverFactory) {
                // 1. mock Server
                Server mockServer = serverFactory.create(routeInfoImpl);

                // 2. 清空
                instances.clear();

                // 3. 重新添加
                instances.add(mockServer);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
