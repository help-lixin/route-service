package help.lixin.route.config;

import com.netflix.discovery.EurekaClient;
import help.lixin.route.core.discovery.DiscoveryClientTemplate;
import help.lixin.route.core.discovery.IDiscoveryClientTemplate;
import help.lixin.route.core.discovery.eureak.EurekaDiscoveryClientExt;
import help.lixin.route.core.discovery.eureak.EurekaReactiveDiscoveryClientExt;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.cloud.netflix.eureka.reactive.EurekaReactiveDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient")
public class EurekaClientConfig {

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnClass(name = "org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient")
    public EurekaDiscoveryClient discoveryClient(EurekaClient client, com.netflix.discovery.EurekaClientConfig clientConfig, IDiscoveryClientTemplate discoveryClientTemplate) {
        return new EurekaDiscoveryClientExt(client, clientConfig, discoveryClientTemplate);
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(name = "org.springframework.cloud.netflix.eureka.reactive.EurekaReactiveDiscoveryClient")
    public EurekaReactiveDiscoveryClient eurekaReactiveDiscoveryClient(EurekaClient client,
                                                                       //
                                                                       IDiscoveryClientTemplate discoveryClientTemplate,
                                                                       //
                                                                       com.netflix.discovery.EurekaClientConfig clientConfig) {
        return new EurekaReactiveDiscoveryClientExt(client, clientConfig, discoveryClientTemplate);
    }
}
