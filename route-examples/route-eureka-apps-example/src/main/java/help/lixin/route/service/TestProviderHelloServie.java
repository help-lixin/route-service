package help.lixin.route.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("test-provider")
public interface TestProviderHelloServie {

	@GetMapping("/hello")
	String hello();
}
