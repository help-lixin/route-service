package help.lixin.route.consumer1;

import help.lixin.route.service.TestProviderHelloServie;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//@EnableEurekaClient
@EnableFeignClients(basePackageClasses = TestProviderHelloServie.class)
@SpringBootApplication(scanBasePackages = "help.lixin.route.consumer1")
public class ConsumerOneApplication {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active","consumer1");
        SpringApplication.run(ConsumerOneApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
    
}
