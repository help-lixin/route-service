package help.lixin.route.gateway;

import com.alibaba.cloud.nacos.discovery.reactive.NacosReactiveDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootConfiguration
@EnableAutoConfiguration
public class GatewayApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(GatewayApplication.class, args);
		NacosReactiveDiscoveryClient bean = context.getBean(NacosReactiveDiscoveryClient.class);
		System.out.println(bean);
	}
}
