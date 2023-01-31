package help.lixin.route.core.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import help.lixin.route.constants.Constants;
import help.lixin.route.core.context.RouteInfoHolder;
import help.lixin.route.core.context.XRouteHolder;
import help.lixin.route.model.RouteInfoList;
import help.lixin.route.core.parse.RouteParseServiceFace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 拦截请求,获取协议头中的信息,转换成业务模型(RouteInfo),并设置到线程上下文中.
 */
public class RouteFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(RouteFilter.class);

	private RouteParseServiceFace routeParseServiceFace;

	public void setRouteParseServiceFace(RouteParseServiceFace routeParseServiceFace) {
		this.routeParseServiceFace = routeParseServiceFace;
	}

	public RouteParseServiceFace getRouteParseServiceFace() {
		return routeParseServiceFace;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		boolean isEnabledRouteString = false;
		boolean isEnabledRouteInfoCollection = false;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			// 1. 获得协议头里的路由信息(x-route)
			String routeString = httpRequest.getHeader(Constants.ROUTE_KEY);
			if (null != routeString) {
				RouteInfoList routeInfoList = null;
				try {
					// 2. 解析路由信息,转换成业务模型(RouteInfo)
					routeInfoList = routeParseServiceFace.transform(routeString);
				} catch (Throwable e) {
					logger.warn("parse route info error:[{}]", e);
				}

				if (null != routeInfoList && !routeInfoList.getRouteInfos().isEmpty()) {
					isEnabledRouteString = true;
					isEnabledRouteInfoCollection = true;
					// 3. 把业务模型信息,与线和上下文进行绑定
					RouteInfoHolder.set(routeInfoList);

					// 4. 把协议头里的信息继续保存在线程上下文中,是因为:这个信息(x-route)还得要通这RestTemplate(RouteRequestInterceptor)进行继续传递.
					XRouteHolder.set(routeString);

					if (logger.isDebugEnabled()) {
						logger.debug("bind rules:[{}] to context", routeInfoList);
					}
				}
			}
		}

		try {
			chain.doFilter(request, response);
		} finally {
			if (isEnabledRouteString) {
				XRouteHolder.clean();
			}
			if (isEnabledRouteInfoCollection) {
				RouteInfoHolder.clean();
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
