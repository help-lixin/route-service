package help.lixin.route.config;

import java.util.List;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import help.lixin.route.meta.IRouteService;
import help.lixin.route.meta.RouteServiceFace;
import help.lixin.route.meta.RouteServiceMediator;
import help.lixin.route.meta.impl.RewriteRouteService;
import help.lixin.route.parse.IRouteParseService;
import help.lixin.route.parse.RouteParseServiceFace;
import help.lixin.route.parse.impl.RewriteRouteParseService;
import help.lixin.route.ribbon.RibbonLoadBalancerClientProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.util.ProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

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

    /**
     * 进行路由重写的门面模式(入口)
     *
     * @return
     */
    @Bean
    public RouteServiceFace routeServiceFace() {
        return new RouteServiceFace();
    }

    /**
     * 根据协议头信息,进行路由重写
     *
     * @return
     */
    @Bean
    public IRouteService rewriteRouteService() {
        return new RewriteRouteService();
    }

    /**
     * 中介者模式,存储对协议头解析后的对象(RouteInfo)与路由(IRouteService)之间的关系
     * IRouteService需要设置支持对:RouteInfo类的解析.
     *
     * @param routeServices
     * @return
     */
    @Bean
    public RouteServiceMediator routeServiceMediator(ObjectProvider<List<IRouteService>> routeServices) {
        RouteServiceMediator routeServiceMediator = RouteServiceMediator.getInstance();
        List<IRouteService> routeList = routeServices.getIfAvailable();
        for (IRouteService routeService : routeList) {
            routeServiceMediator.put(routeService.supportType(), routeService);
        }
        return routeServiceMediator;
    }
}
