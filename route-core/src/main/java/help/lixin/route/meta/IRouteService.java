package help.lixin.route.meta;

import help.lixin.route.meta.ctx.RouteInfoContext;
import org.springframework.cloud.client.ServiceInstance;


public interface IRouteService {

	/**
	 * @param ctx 承载RouteInfo信息,允许开发自定义处理.
	 * @return
	 */
	ServiceInstance getServiceInstance(RouteInfoContext ctx);

	/**
	 * 支持的路由类型
	 * 
	 * @return
	 */
	Class<?> supportType();
}
