package help.lixin.route.config;

import com.netflix.discovery.EurekaClient;
import help.lixin.route.core.discovery.eureak.EurekaDiscoveryClientExt;
import help.lixin.route.core.discovery.eureak.EurekaReactiveDiscoveryClientExt;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.cloud.netflix.eureka.reactive.EurekaReactiveDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient.class)
public class EurekaClientConfig {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnClass(org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient.class)
    public EurekaDiscoveryClient discoveryClient(EurekaClient client, com.netflix.discovery.EurekaClientConfig clientConfig, IServiceInstanceFilterFace serviceInstanceFilterFace) {
        return new EurekaDiscoveryClientExt(client, clientConfig, serviceInstanceFilterFace);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(org.springframework.cloud.netflix.eureka.reactive.EurekaReactiveDiscoveryClient.class)
    public EurekaReactiveDiscoveryClient eurekaReactiveDiscoveryClient(EurekaClient client,
                                                                       //
                                                                       IServiceInstanceFilterFace serviceInstanceFilterFace,
                                                                       //
                                                                       com.netflix.discovery.EurekaClientConfig clientConfig) {
        return new EurekaReactiveDiscoveryClientExt(client, clientConfig, serviceInstanceFilterFace);
    }
}
