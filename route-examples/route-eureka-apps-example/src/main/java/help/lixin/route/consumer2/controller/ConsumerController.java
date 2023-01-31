package help.lixin.route.consumer2.controller;

import help.lixin.route.consumer2.service.HelloService;
import help.lixin.route.model.User;
import help.lixin.route.service.TestProviderHelloServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
	@Autowired
	private HelloService helloService;

	@Autowired
	private TestProviderHelloServie testProviderHelloServie;

	@GetMapping("/consumer")
	public String index(@RequestHeader(value = "x-route", required = false) String xroute) {
		System.out.println("******************************x-route*****************************" + xroute);
		return helloService.hello();
	}

	@GetMapping("/hello")
	public String hello() {
		return testProviderHelloServie.hello();
	}

	@PostMapping("/save-1")
	public User save1(@RequestBody User user) {
		user.setAge(18);
		return user;
	}

	@PostMapping("/save-2")
	public User save2(User user) {
		user.setAge(18);
		return user;
	}
}
