package help.lixin.route.config;

import java.util.List;

import com.netflix.loadbalancer.Server;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.parse.IRouteParseService;
import help.lixin.route.core.parse.RouteParseServiceFace;
import help.lixin.route.core.parse.impl.RewriteRouteParseService;
import help.lixin.route.filter.IServerFactory;
import help.lixin.route.filter.IServerFilter;
import help.lixin.route.filter.IServerFilterFace;
import help.lixin.route.filter.ServerFilterFace;
import help.lixin.route.filter.impl.NacosServerFactory;
import help.lixin.route.filter.impl.RewriteEurekaRouteFilter;
import help.lixin.route.ribbon.RibbonClientAutoRegister;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
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
     * 对它的扩展:<br/>
     *
     * @return
     * @RibbonClients(defaultConfiguration=XXX.class)<br/> 因为:它需要明确指定Class,而我这里的,Class会根据是nacos/eureka来指定.<br/>
     */
    @Bean
    public BeanFactoryPostProcessor ribbonClientAutoRegister() {
        BeanFactoryPostProcessor register = new RibbonClientAutoRegister();
        return register;
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
    public IServerFilterFace<Server> serverFilterFace(@Autowired(required = false) List<IServerFilter<Server>> serverFilters) {
        ServerFilterFace serverFilterFace = new ServerFilterFace();
        if (null != serverFilters) {
            serverFilterFace.setFilterList(serverFilters);
        }
        return serverFilterFace;
    }

    /**
     * Filter的实现之一(主要是实现:对路由的重写)
     *
     * @return
     */
    @Bean
    public IServerFilter<Server> rewriteEurekaRouteFilter() {
        IServerFilter<Server> rewriteEurekaRouteFilter = new RewriteEurekaRouteFilter();
        return rewriteEurekaRouteFilter;
    }
}
