package help.lixin.route.config;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import help.lixin.route.constants.Constants;
import help.lixin.route.filter.IServerFactory;
import help.lixin.route.factory.NacosServerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(NacosDiscoveryClient.class)
public class NacosRewriteServerConfig {

    @Bean(name = Constants.DISCOVERY_NACOS)
    public IServerFactory nacosServerFactory() {
        return new NacosServerFactory();
    }
}
