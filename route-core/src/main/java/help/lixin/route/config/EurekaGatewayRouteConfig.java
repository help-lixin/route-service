package help.lixin.route.config;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.loadbalancer.Server;
import com.netflix.discovery.EurekaClient;
import help.lixin.route.filter.IServerFilterFace;
import help.lixin.route.gateway.EurekaLoadBalancerClientExtFilter;
import help.lixin.route.core.parse.RouteParseServiceFace;
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
 */
@SuppressWarnings("deprecation")
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableConfigurationProperties(LoadBalancerProperties.class)
@AutoConfigureAfter(RibbonAutoConfiguration.class)
@ConditionalOnClass({LoadBalancerClient.class, RibbonAutoConfiguration.class, DispatcherHandler.class, DiscoveryClient.class})
public class EurekaGatewayRouteConfig {

    @Bean
    @ConditionalOnBean({EurekaClient.class, LoadBalancerClient.class})
    @ConditionalOnClass(GatewayAutoConfiguration.class)
    public LoadBalancerClientFilter loadBalancerClientExtFilter(
            //
            LoadBalancerClient client,
            //
            LoadBalancerProperties properties,
            //
            RouteParseServiceFace routeParseServiceFace,
            //
            IServerFilterFace<Server> serverFilterFace,
            //
            EurekaClient eurekaClient) {
        EurekaLoadBalancerClientExtFilter filter = new EurekaLoadBalancerClientExtFilter(client, properties);
        filter.setRouteParseServiceFace(routeParseServiceFace);
        filter.setServerFilterFace(serverFilterFace);
        filter.setEurekaClient(eurekaClient);
        return filter;
    }
}