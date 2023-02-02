package help.lixin.route.config;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import com.netflix.loadbalancer.Server;
import help.lixin.route.core.parse.RouteParseServiceFace;
import help.lixin.route.filter.IServerFilterFace;
import help.lixin.route.gateway.NacosLoadBalancerClientExtFilter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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
@ConditionalOnClass({LoadBalancerClient.class, RibbonAutoConfiguration.class, DispatcherHandler.class, NacosDiscoveryClient.class})
public class NacosGatewayRouteConfig {

    @Bean
    @ConditionalOnBean({DiscoveryClient.class, LoadBalancerClient.class})
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
            DiscoveryClient discoveryClient) {
        NacosLoadBalancerClientExtFilter filter = new NacosLoadBalancerClientExtFilter(client, properties);
        filter.setRouteParseServiceFace(routeParseServiceFace);
        filter.setServerFilterFace(serverFilterFace);
        filter.setDiscoveryClient(discoveryClient);
        return filter;
    }
}