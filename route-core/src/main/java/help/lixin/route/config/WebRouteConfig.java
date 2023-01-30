package help.lixin.route.config;

import java.util.ArrayList;
import java.util.List;

import help.lixin.route.parse.RouteParseServiceFace;
import help.lixin.route.rest.RouteRequestInterceptor;
import help.lixin.route.servlet.RouteFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * 针对webmvc的配置
 *
 * @author lixin
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnMissingClass("org.springframework.cloud.gateway.config.GatewayAutoConfiguration")
public class WebRouteConfig {
    private Logger logger = LoggerFactory.getLogger(WebRouteConfig.class);


    /**
     * 添加Filter,负责把x-route信息转换成业务模型,并绑定到ThredLocal里.
     *
     * @return
     */
    @Bean
    public RouteFilter routeFilter(RouteParseServiceFace routeParseServiceFace) {
        RouteFilter routeFilter = new RouteFilter();
        routeFilter.setRouteParseServiceFace(routeParseServiceFace);
        return routeFilter;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Bean
    public FilterRegistrationBean registerRouteFilter(RouteFilter routeFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(routeFilter);
        registration.addUrlPatterns("/*");
        registration.setName("routeFilter");
        registration.setOrder(1); // 值越小，Filter越靠前。
        return registration;
    }


    @Configuration
    @ConditionalOnClass(RestTemplate.class)
    @ConditionalOnBean(RestTemplate.class)
    public class ClientHttpRequestInterceptorConfig {
        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        @Qualifier("routeRequestInterceptor")
        private ClientHttpRequestInterceptor routeRequestInterceptor;

        @PostConstruct
        public void customer() {
            if (null != restTemplate && null != routeRequestInterceptor) {
                List<ClientHttpRequestInterceptor> list = new ArrayList<ClientHttpRequestInterceptor>(
                        restTemplate.getInterceptors());
                list.add(routeRequestInterceptor);
                if (logger.isDebugEnabled()) {
                    logger.debug("add [RouteRequestInterceptor] to [RestTemplate] SUCCESS");
                }
                restTemplate.setInterceptors(list);
            }
        }
    }

    @Bean
    @ConditionalOnClass(RestTemplate.class)
    @ConditionalOnBean(RestTemplate.class)
    public ClientHttpRequestInterceptor routeRequestInterceptor() {
        return new RouteRequestInterceptor();
    }
}