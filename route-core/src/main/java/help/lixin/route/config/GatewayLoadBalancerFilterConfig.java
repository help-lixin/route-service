package help.lixin.route.config;

import help.lixin.route.core.parse.RouteParseServiceFace;
import help.lixin.route.core.gateway.ReactiveLoadBalancerClientFilterExt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 针对gateway的配置
 *
 * @author lixin
 */
@SuppressWarnings("deprecation")
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GatewayLoadBalancerFilterConfig {

    @Bean
    public ReactiveLoadBalancerClientFilter gatewayLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory,
                                                                            //
                                                                            RouteParseServiceFace routeParseServiceFace,
                                                                            //
                                                                            GatewayLoadBalancerProperties properties,
                                                                            //
                                                                            LoadBalancerProperties loadBalancerProperties) {
        return new ReactiveLoadBalancerClientFilterExt(clientFactory, properties, loadBalancerProperties, routeParseServiceFace);
    }
}