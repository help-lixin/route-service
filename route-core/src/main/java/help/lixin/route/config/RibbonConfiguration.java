package help.lixin.route.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import help.lixin.route.ribbon.ZoneAwareLoadBalancerExt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class RibbonConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ILoadBalancer ribbonLoadBalancer(IClientConfig config,
                                            ServerList<Server> serverList, ServerListFilter<Server> serverListFilter,
                                            IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
        return new ZoneAwareLoadBalancerExt(config, rule, ping, serverList,
                serverListFilter, serverListUpdater);
    }
}
