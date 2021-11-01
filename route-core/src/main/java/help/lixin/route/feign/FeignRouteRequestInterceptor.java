package help.lixin.route.feign;

import help.lixin.route.constants.Constants;
import help.lixin.route.context.RouteInfoHolder;
import help.lixin.route.context.XRouteHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignRouteRequestInterceptor implements RequestInterceptor {

	private Logger logger = LoggerFactory.getLogger(FeignRouteRequestInterceptor.class);

	@Override
	public void apply(RequestTemplate template) {
		// 1. 判断是否启用了路由
		if (RouteInfoHolder.isEnabled()) {
			String xrouteValue = XRouteHolder.get();
			template.header(Constants.ROUTE_KEY, xrouteValue);
			if (logger.isDebugEnabled()) {
				logger.debug("add [{}:{}] to request header", Constants.ROUTE_KEY, xrouteValue);
			}
		}
	}
}
