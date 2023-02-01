package help.lixin.route.config;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import com.netflix.client.config.IClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.loadbalancer.*;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import help.lixin.route.constants.Constants;
import help.lixin.route.filter.IServerFilterFace;
import help.lixin.route.ribbon.DiscoveryEnabledNIWSServerListExt;
import help.lixin.route.ribbon.ZoneAwareLoadBalancerExt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServerList;
import org.springframework.context.annotation.Bean;

import javax.inject.Provider;

public class RibbonConfiguration {

    @Bean
    @ConditionalOnClass(com.netflix.discovery.DiscoveryClient.class)
    @ConditionalOnBean(com.netflix.discovery.DiscoveryClient.class)
    public ServerList<?> eurekaRibbonServerList(IClientConfig config,
                                                //
                                                IServerFilterFace<Server> serverFilterFace,
                                                //
                                                Provider<EurekaClient> eurekaClientProvider) {
        DiscoveryEnabledNIWSServerListExt discoveryServerList = new DiscoveryEnabledNIWSServerListExt(config, eurekaClientProvider);
        discoveryServerList.setServerFilterFace(serverFilterFace);
        discoveryServerList.setType(Constants.DISCOVERY_EUREKA);

        DomainExtractingServerList serverList = new DomainExtractingServerList(discoveryServerList, config, true);
        return serverList;
    }

    @Bean
    @ConditionalOnClass(com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient.class)
    public ServerList<?> nacosRibbonServerList(IClientConfig config,
                                               //
                                               IServerFilterFace<Server> serverFilterFace,
                                               //
                                               Provider<EurekaClient> eurekaClientProvider) {
        DiscoveryEnabledNIWSServerListExt discoveryServerList = new DiscoveryEnabledNIWSServerListExt(config, eurekaClientProvider);
        discoveryServerList.setServerFilterFace(serverFilterFace);
        discoveryServerList.setType(Constants.DISCOVERY_NACOS);
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
