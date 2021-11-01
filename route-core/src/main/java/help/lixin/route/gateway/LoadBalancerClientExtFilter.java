package help.lixin.route.gateway;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

import java.net.URI;

import help.lixin.route.constants.Constants;
import help.lixin.route.meta.RouteServiceFace;
import help.lixin.route.meta.ctx.RouteInfoContext;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfoList;
import help.lixin.route.parse.RouteParseServiceFace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.web.server.ServerWebExchange;

@SuppressWarnings("deprecation")
public class LoadBalancerClientExtFilter extends LoadBalancerClientFilter {
	private Logger logger = LoggerFactory.getLogger(LoadBalancerClientExtFilter.class);

	private RouteParseServiceFace routeParseServiceFace;

	private RouteServiceFace routeServiceFace;

	public LoadBalancerClientExtFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
		super(loadBalancer, properties);
	}

	public void setRouteServiceFace(RouteServiceFace routeServiceFace) {
		this.routeServiceFace = routeServiceFace;
	}

	public RouteServiceFace getRouteServiceFace() {
		return routeServiceFace;
	}

	public void setRouteParseServiceFace(RouteParseServiceFace routeParseServiceFace) {
		this.routeParseServiceFace = routeParseServiceFace;
	}

	public RouteParseServiceFace getRouteParseServiceFace() {
		return routeParseServiceFace;
	}

	@Override
	protected ServiceInstance choose(ServerWebExchange exchange) {
		// 1. 获得要请求的微服务名称
		String serviceId = ((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost().toLowerCase();
		// 2. 获得协议头上的信息
		String routeString = exchange.getRequest().getHeaders().getFirst(Constants.ROUTE_KEY);
		if (null != routeString) {
			// 转换字符串信息到业务模型,如果转换出现了问题,也不要影响业务正常流转.
			RouteInfoList routeInfoCollection = null;
			try {
				// 3. 对协议头里的信息进行解析,转换成业务模型.
				routeInfoCollection = routeParseServiceFace.transform(routeString);
			} catch (Throwable e) {
				logger.warn("parse route info error:[{}]", e);
			}

			if (null != routeInfoCollection && !routeInfoCollection.getRouteInfos().isEmpty()) {
				IRouteInfo routeInfo = routeInfoCollection.getRouteInfos().get(serviceId);
				if (null != routeInfo) {
					// 4. 构建路由信息的上下文.
					RouteInfoContext ctx = RouteInfoContext.newBuilder().routeInfo(routeInfo).build();
					// 5. 委托给路由门面进行处理.
					ServiceInstance serviceInstance = routeServiceFace.getServiceInstance(ctx);
					if (null != serviceId) {
						return serviceInstance;
					}
				}
			}
		}
		return super.choose(exchange);
	}
}
