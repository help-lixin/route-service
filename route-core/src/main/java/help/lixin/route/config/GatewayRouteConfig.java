package help.lixin.route.config;

import help.lixin.route.gateway.LoadBalancerClientExtFilter;
import help.lixin.route.meta.RouteServiceFace;
import help.lixin.route.parse.RouteParseServiceFace;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;

/**
 * 针对gateway的配置
 * 
 * @author lixin
 *
 */
@SuppressWarnings("deprecation")
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableConfigurationProperties(LoadBalancerProperties.class)
@AutoConfigureAfter(RibbonAutoConfiguration.class)
@ConditionalOnClass({ LoadBalancerClient.class, RibbonAutoConfiguration.class, DispatcherHandler.class })
public class GatewayRouteConfig {

	@Bean
	@ConditionalOnBean(LoadBalancerClient.class)
	@ConditionalOnClass(GatewayAutoConfiguration.class)
	public LoadBalancerClientFilter loadBalancerClientFilter(
			LoadBalancerClient client,
			LoadBalancerProperties properties, 
			RouteParseServiceFace routeParseServiceFace,
			RouteServiceFace routeServiceFace) {
		LoadBalancerClientExtFilter filter = new LoadBalancerClientExtFilter(client, properties);
		filter.setRouteParseServiceFace(routeParseServiceFace);
		filter.setRouteServiceFace(routeServiceFace);
		return filter;
	}
}