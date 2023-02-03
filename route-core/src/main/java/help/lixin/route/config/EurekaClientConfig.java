package help.lixin.route.config;

import com.netflix.discovery.EurekaClient;
import help.lixin.route.core.discovery.eureak.EurekaDiscoveryClientExt;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient.class)
public class EurekaClientConfig {

    @Bean
    @ConditionalOnMissingBean
    public EurekaDiscoveryClient discoveryClient(EurekaClient client, com.netflix.discovery.EurekaClientConfig clientConfig, IServiceInstanceFilterFace serviceInstanceFilterFace) {
        return new EurekaDiscoveryClientExt(client, clientConfig, serviceInstanceFilterFace);
    }
}
