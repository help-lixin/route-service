package help.lixin.route.config;

import com.netflix.loadbalancer.Server;
import help.lixin.route.filter.IServerFilter;
import help.lixin.route.filter.ServerFilterFace;
import help.lixin.route.filter.impl.RewriteEurekaRouteFilter;
import help.lixin.route.filter.IServerFilterFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnClass(com.netflix.discovery.EurekaClient.class)
public class EurekaClientConfig {
    // 拦截器
    @Bean
    public IServerFilter<Server> rewriteEurekaRouteFilter() {
        IServerFilter<Server> rewriteEurekaRouteFilter = new RewriteEurekaRouteFilter();
        return rewriteEurekaRouteFilter;
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
