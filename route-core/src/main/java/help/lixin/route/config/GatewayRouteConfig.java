package help.lixin.route.config;

import com.netflix.discovery.DiscoveryClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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
@ConditionalOnClass({LoadBalancerClient.class, DispatcherHandler.class, DiscoveryClient.class})
public class GatewayRouteConfig {

//    @Bean
//    @ConditionalOnBean({EurekaClient.class, LoadBalancerClient.class})
//    @ConditionalOnClass(GatewayAutoConfiguration.class)
//    public LoadBalancerClientFilter loadBalancerClientExtFilter(
//            //
//            LoadBalancerClient client,
//            //
//            LoadBalancerProperties properties,
//            //
//            RouteParseServiceFace routeParseServiceFace,
//            //
//            IServiceInstanceFilterFace<Server> serverFilterFace,
//            //
//            EurekaClient eurekaClient) {
//        EurekaLoadBalancerClientExtFilter filter = new EurekaLoadBalancerClientExtFilter(client, properties);
//        filter.setRouteParseServiceFace(routeParseServiceFace);
//        filter.setServerFilterFace(serverFilterFace);
//        filter.setEurekaClient(eurekaClient);
//        return filter;
//    }
}