package help.lixin.route.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootConfiguration
@EnableAutoConfiguration
public class GatewayApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(GatewayApplication.class, args);
		DiscoveryClient client = applicationContext.getBean(DiscoveryClient.class);
		System.out.println(client);
	}
}
