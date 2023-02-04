package help.lixin.route.config;

import com.tencent.cloud.polaris.discovery.PolarisServiceDiscovery;
import com.tencent.cloud.polaris.discovery.reactive.PolarisReactiveDiscoveryClient;
import help.lixin.route.core.discovery.DiscoveryClientTemplate;
import help.lixin.route.core.discovery.IDiscoveryClientTemplate;
import help.lixin.route.core.discovery.polaris.PolarisDiscoveryClientExt;
import help.lixin.route.core.discovery.polaris.PolarisReactiveDiscoveryClientExt;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "com.tencent.cloud.polaris.discovery.PolarisDiscoveryClient")
public class PolarisClientConfig {

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnClass(name = "com.tencent.cloud.polaris.discovery.PolarisDiscoveryClient")
    public DiscoveryClient polarisDiscoveryClient(PolarisServiceDiscovery polarisServiceDiscovery,
                                                  //
                                                  IDiscoveryClientTemplate discoveryClientTemplate) {
        return new PolarisDiscoveryClientExt(polarisServiceDiscovery, discoveryClientTemplate);
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(name = "com.tencent.cloud.polaris.discovery.reactive.PolarisReactiveDiscoveryClient")
    public PolarisReactiveDiscoveryClient polarisReactiveDiscoveryClient(PolarisServiceDiscovery polarisServiceDiscovery,
                                                                         //
                                                                         IDiscoveryClientTemplate discoveryClientTemplate) {
        return new PolarisReactiveDiscoveryClientExt(polarisServiceDiscovery, discoveryClientTemplate);
    }
}
