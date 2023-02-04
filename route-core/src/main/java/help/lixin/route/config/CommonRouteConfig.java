package help.lixin.route.config;

import java.util.List;

import help.lixin.route.core.discovery.DiscoveryClientTemplate;
import help.lixin.route.core.discovery.IDiscoveryClientTemplate;
import help.lixin.route.core.loadbalancer.BlockingLoadBalancerClientExt;
import help.lixin.route.core.parse.IRouteParseService;
import help.lixin.route.core.parse.RouteParseServiceFace;
import help.lixin.route.core.parse.impl.RewriteRouteParseService;
import help.lixin.route.core.serviceid.IServiceIdService;
import help.lixin.route.core.serviceid.impl.ServiceIdService;
import help.lixin.route.filter.IServiceInstanceFilter;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import help.lixin.route.filter.ServiceInstanceFilterFace;
import help.lixin.route.filter.impl.RewriteEurekaRouteFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 路由的公共配置
 *
 * @author lixin
 */
@Configuration
public class CommonRouteConfig {

    @Bean
    public IDiscoveryClientTemplate discoveryClientTemplate(IServiceInstanceFilterFace serviceInstanceFilterFace,
                                                            //
                                                            IServiceIdService serviceIdService) {
        return new DiscoveryClientTemplate(serviceInstanceFilterFace, serviceIdService);
    }

    @Bean
    public IServiceIdService serviceIdService() {
        return new ServiceIdService();
    }


    @Bean
    @ConditionalOnMissingBean
    public LoadBalancerClient blockingLoadBalancerClient(LoadBalancerClientFactory loadBalancerClientFactory,
                                                         //
                                                         RouteParseServiceFace routeParseServiceFace,
                                                         //
                                                         IServiceIdService serviceIdService,
                                                         //
                                                         LoadBalancerProperties properties) {
        return new BlockingLoadBalancerClientExt(loadBalancerClientFactory, properties, routeParseServiceFace, serviceIdService);
    }

    /**
     * 解析路由服务(重写路由)
     *
     * @return
     */
    @Bean
    public IRouteParseService rewriteRouteParseService() {
        return new RewriteRouteParseService();
    }

    /**
     * 路由解析门面(入口)
     *
     * @param routeParseServices
     * @return
     */
    @Bean
    public RouteParseServiceFace routeParseServiceFace(ObjectProvider<List<IRouteParseService>> routeParseServices) {
        RouteParseServiceFace routeParseServiceFace = new RouteParseServiceFace();
        List<IRouteParseService> chains = routeParseServices.getIfAvailable();
        routeParseServiceFace.setChains(chains);
        return routeParseServiceFace;
    }

    /**
     * Filter门面模式
     *
     * @param serverFilters
     * @return
     */
    @Bean
    public IServiceInstanceFilterFace<ServiceInstance> serviceInstanceFilterFace(@Autowired(required = false) List<IServiceInstanceFilter<ServiceInstance>> serverFilters) {
        ServiceInstanceFilterFace serviceInstanceFilterFace = new ServiceInstanceFilterFace();
        if (null != serverFilters) {
            serviceInstanceFilterFace.setFilterList(serverFilters);
        }
        return serviceInstanceFilterFace;
    }

    /**
     * Filter的实现之一(主要是实现:对路由的重写)
     *
     * @return
     */
    @Bean
    public IServiceInstanceFilter<ServiceInstance> rewriteEurekaRouteFilter() {
        IServiceInstanceFilter<ServiceInstance> rewriteEurekaRouteFilter = new RewriteEurekaRouteFilter();
        return rewriteEurekaRouteFilter;
    }
}
