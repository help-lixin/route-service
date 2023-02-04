package help.lixin.route.config;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.cloud.nacos.discovery.reactive.NacosReactiveDiscoveryClient;
import help.lixin.route.core.discovery.DiscoveryClientTemplate;
import help.lixin.route.core.discovery.IDiscoveryClientTemplate;
import help.lixin.route.core.discovery.nacos.NacosDiscoveryClientExt;
import help.lixin.route.core.discovery.nacos.NacosReactiveDiscoveryClientExt;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient")
public class NacosClientConfig {
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnClass(name = "com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient")
    public DiscoveryClient nacosDiscoveryClient(NacosServiceDiscovery nacosServiceDiscovery, IDiscoveryClientTemplate discoveryClientTemplate) {
        return new NacosDiscoveryClientExt(nacosServiceDiscovery, discoveryClientTemplate);
    }


    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(name = "com.alibaba.cloud.nacos.discovery.reactive.NacosReactiveDiscoveryClient")
    public NacosReactiveDiscoveryClient nacosReactiveDiscoveryClient(NacosServiceDiscovery nacosServiceDiscovery, IDiscoveryClientTemplate discoveryClientTemplate) {
        return new NacosReactiveDiscoveryClientExt(nacosServiceDiscovery, discoveryClientTemplate);
    }
}
