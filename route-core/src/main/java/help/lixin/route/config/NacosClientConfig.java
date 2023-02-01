package help.lixin.route.config;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(value = NacosDiscoveryClient.class)
public class NacosClientConfig {

}
