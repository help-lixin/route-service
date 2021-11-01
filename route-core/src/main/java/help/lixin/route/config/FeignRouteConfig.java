package help.lixin.route.config;

import help.lixin.route.feign.FeignRouteRequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.eureka.CachingSpringLoadBalancerFactoryExt;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.netflix.loadbalancer.ILoadBalancer;

import feign.Feign;
import feign.RequestInterceptor;

/**
 * 把Feign独立出配置
 * @author lixin
 *
 */
@Configuration
@ConditionalOnClass({ CachingSpringLoadBalancerFactory.class, ILoadBalancer.class, Feign.class })
public class FeignRouteConfig {

	/**
	 * 重写:CachingSpringLoadBalancerFactory,创建自定义的:FeignLoadBalancerExt
	 * 
	 * @param factory
	 * @return
	 */
	@Primary
	@Bean
	public CachingSpringLoadBalancerFactory cachingLBClientFactory(SpringClientFactory factory) {
		return new CachingSpringLoadBalancerFactoryExt(factory);
	}

	/**
	 * Feign透传x-route
	 * 
	 * @return
	 */
	@Bean
	public RequestInterceptor feignRouteRequestInterceptor() {
		return new FeignRouteRequestInterceptor();
	}
}
