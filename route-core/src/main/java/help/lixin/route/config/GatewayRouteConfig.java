package help.lixin.route.config;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import help.lixin.route.core.discovery.eureak.EurekaReactiveDiscoveryClientExt;
import help.lixin.route.core.parse.RouteParseServiceFace;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import help.lixin.route.gateway.ReactiveLoadBalancerClientFilterExt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledGlobalFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.netflix.eureka.reactive.EurekaReactiveDiscoveryClient;
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
@ConditionalOnClass({LoadBalancerClient.class, DispatcherHandler.class, DiscoveryClient.class})
public class GatewayRouteConfig {


    @Bean
    @ConditionalOnMissingBean
    public EurekaReactiveDiscoveryClient eurekaReactiveDiscoveryClient(EurekaClient client,
                                                                       //
                                                                       IServiceInstanceFilterFace serviceInstanceFilterFace,
                                                                       //
                                                                       EurekaClientConfig clientConfig) {
        return new EurekaReactiveDiscoveryClientExt(client, clientConfig, serviceInstanceFilterFace);
    }

    @Bean
    // @ConditionalOnMissingBean(ReactiveLoadBalancerClientFilter.class)
    @ConditionalOnEnabledGlobalFilter
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