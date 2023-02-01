package help.lixin.route.ribbon.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.ConditionalOnRibbonNacos;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import help.lixin.route.filter.IServerFilterFace;
import help.lixin.route.ribbon.NacosServerListExt;
import help.lixin.route.ribbon.ZoneAwareLoadBalancerExt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class NacosRibbonClientConfig {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnRibbonNacos
    public ServerList<?> nacosRibbonServerList(IClientConfig config,
                                               //
                                               IServerFilterFace<Server> serverFilterFace,
                                               //
                                               NacosDiscoveryProperties nacosDiscoveryProperties) {
        NacosServerListExt serverList = new NacosServerListExt(nacosDiscoveryProperties);
        serverList.setServerFilterFace(serverFilterFace);
        serverList.initWithNiwsConfig(config);
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
