package help.lixin.route.config;

import java.util.List;

import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.parse.IRouteParseService;
import help.lixin.route.core.parse.RouteParseServiceFace;
import help.lixin.route.core.parse.impl.RewriteRouteParseService;
import help.lixin.route.filter.IServerFactory;
import help.lixin.route.filter.IServerFilter;
import help.lixin.route.filter.IServerFilterFace;
import help.lixin.route.filter.ServerFilterFace;
import help.lixin.route.filter.impl.EurekaServerFactory;
import help.lixin.route.filter.impl.NacosServerFactory;
import help.lixin.route.filter.impl.RewriteEurekaRouteFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 路由的公共配置
 *
 * @author lixin
 */
@Configuration
public class CommonRouteConfig {
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

    // 拦截器 TODO lixin
    @Bean
    public IServerFilter<Server> rewriteEurekaRouteFilter() {
        IServerFilter<Server> rewriteEurekaRouteFilter = new RewriteEurekaRouteFilter();
        return rewriteEurekaRouteFilter;
    }

//    @Bean(name = Constants.DISCOVERY_EUREKA)
//    public IServerFactory eurekaServerFactory() {
//        return new EurekaServerFactory();
//    }

    @Bean(name = Constants.DISCOVERY_NACOS)
    public IServerFactory nacosServerFactory() {
        return new NacosServerFactory();
    }


    @Bean
    public IServerFilterFace<Server> serverFilterFace(@Autowired(required = false) List<IServerFilter<Server>> serverFilters) {
        ServerFilterFace eurekaInstanceFilterFace = new ServerFilterFace();
        if (null != serverFilters) {
            eurekaInstanceFilterFace.setFilterList(serverFilters);
        }
        return eurekaInstanceFilterFace;
    }
}
