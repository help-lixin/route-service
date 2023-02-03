package help.lixin.route.config;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.cloud.nacos.discovery.reactive.NacosReactiveDiscoveryClient;
import help.lixin.route.core.discovery.nacos.NacosDiscoveryClientExt;
import help.lixin.route.core.discovery.nacos.NacosReactiveDiscoveryClientExt;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NacosClientConfig {

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnClass(com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient.class)
    public DiscoveryClient nacosDiscoveryClient(NacosServiceDiscovery nacosServiceDiscovery, IServiceInstanceFilterFace serviceInstanceFilterFace) {
        return new NacosDiscoveryClientExt(nacosServiceDiscovery, serviceInstanceFilterFace);
    }


    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(com.alibaba.cloud.nacos.discovery.reactive.NacosReactiveDiscoveryClient.class)
    public NacosReactiveDiscoveryClient nacosReactiveDiscoveryClient(NacosServiceDiscovery nacosServiceDiscovery, IServiceInstanceFilterFace serviceInstanceFilterFace) {
        return new NacosReactiveDiscoveryClientExt(nacosServiceDiscovery, serviceInstanceFilterFace);
    }
}
