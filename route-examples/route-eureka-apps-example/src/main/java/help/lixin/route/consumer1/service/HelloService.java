package help.lixin.route.consumer1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HelloService {
	private Logger logger = LoggerFactory.getLogger(HelloService.class);

	@Autowired
	private RestTemplate restTemplate;

	public String hello() {
		logger.info("====================trace1====================");
		String localStr = "consumer ONE...";
		String url = "http://test-provider/hello";
		String result = restTemplate.getForEntity(url, String.class).getBody();
		return localStr + result;
	}
}
