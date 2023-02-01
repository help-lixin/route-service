package help.lixin.route.ribbon.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.discovery.EurekaClient;
import com.netflix.loadbalancer.*;
import help.lixin.route.filter.IServerFilterFace;
import help.lixin.route.ribbon.DiscoveryEnabledNIWSServerListExt;
import help.lixin.route.ribbon.ZoneAwareLoadBalancerExt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServerList;
import org.springframework.context.annotation.Bean;

import javax.inject.Provider;

public class EurekaRibbonClientConfig {
    @Bean
    public ServerList<?> serverList(IClientConfig config,
                                          //
                                          IServerFilterFace<Server> serverFilterFace,
                                          //
                                          Provider<EurekaClient> eurekaClientProvider) {
        DiscoveryEnabledNIWSServerListExt discoveryServerList = new DiscoveryEnabledNIWSServerListExt(config, eurekaClientProvider);
        discoveryServerList.setServerFilterFace(serverFilterFace);
        DomainExtractingServerList serverList = new DomainExtractingServerList(discoveryServerList, config, true);
        return serverList;
    }

    @Bean
    @ConditionalOnMissingBean
    public ILoadBalancer ribbonLoadBalancer(IClientConfig config,
                                            //
                                            ServerList<Server> serverList,
                                            //
                                            ServerListFilter<Server> serverListFilter,
                                            //
                                            IRule rule,
                                            //
                                            IPing ping,
                                            //
                                            ServerListUpdater serverListUpdater) {
        return new ZoneAwareLoadBalancerExt(config, rule, ping, serverList, serverListFilter, serverListUpdater);
    }
}
