package help.lixin.route.config;

import help.lixin.route.transmit.feign.FeignRouteRequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Feign;
import feign.RequestInterceptor;

/**
 * 把Feign独立出配置
 *
 * @author lixin
 */
@Configuration
@ConditionalOnClass({Feign.class})
public class FeignRouteConfig {

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
