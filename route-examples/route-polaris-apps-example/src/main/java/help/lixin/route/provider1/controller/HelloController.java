package help.lixin.route.provider1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	private Logger logger = LoggerFactory.getLogger(HelloController.class);

	@GetMapping("/hello")
	public String hello() {
		return "Hello World!!! - Provider ONE ";
	}
}