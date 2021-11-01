package help.lixin.route.meta;

import help.lixin.route.meta.ctx.RouteInfoContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;

public class RouteServiceFace {

    private Logger logger = LoggerFactory.getLogger(RouteServiceFace.class);

    public ServiceInstance getServiceInstance(RouteInfoContext ctx) {
        Class<?> clazz = ctx.getRouteInfo().getClass();
        IRouteService routeService = RouteServiceMediator.getInstance().get(clazz);
        if (null != routeService) {
            return routeService.getServiceInstance(ctx);
        } else {
            logger.debug("未找到类[{}],对应的映射(IRouteService)", clazz);
            return null;
        }
    }
}
