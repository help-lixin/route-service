package help.lixin.route.config;

import com.netflix.discovery.DiscoveryClient;
import help.lixin.route.constants.Constants;
import help.lixin.route.filter.IServerFactory;
import help.lixin.route.factory.EurekaServerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(DiscoveryClient.class)
public class EurekaRewriteServerConfig {

    @Bean(name = Constants.DISCOVERY_EUREKA)
    public IServerFactory eurekaServerFactory() {
        return new EurekaServerFactory();
    }
}
