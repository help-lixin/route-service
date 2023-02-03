package help.lixin.route.env;

import java.util.HashMap;
import java.util.Map;

import help.lixin.route.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

/**
 * 在启动Spring之前,可以添加环境变量
 */
public class RouteEnvironmentConfigService implements EnvironmentPostProcessor {
    private Logger logger = LoggerFactory.getLogger(RouteEnvironmentConfigService.class);
    // 允许对Bean的定义信息过时行重写
    private String beanDefinitionOverridingKey = "spring.main.allow-bean-definition-overriding";
    private String beanDefinitionOverridingValue = "true";

    // 排除gateway配置的:LoadBalancerClient
    private String excludeGatewayKey = "spring.autoconfigure.exclude";
    private String excludeValue = "org.springframework.cloud.gateway.config.GatewayLoadBalancerClientAutoConfiguration";
    private final String name = "_overrideRouteEnv";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Boolean isEnabledRoute = environment.getProperty(Constants.CONCAT_ROUTE_ENABLED, Boolean.class);
        if (null != isEnabledRoute && isEnabledRoute) {
            Map<String, Object> frameworkEnvironment = new HashMap<String, Object>();
            // 添加默认的,允许对bean进行重写.
            frameworkEnvironment.put(beanDefinitionOverridingKey, beanDefinitionOverridingValue);
            frameworkEnvironment.put(excludeGatewayKey, excludeValue);
            PropertySource<Map<String, Object>> propertySource = new MapPropertySource(name, frameworkEnvironment);
            environment.getPropertySources().addFirst(propertySource);
            logger.debug("RouteEnvironmentConfigService setting: allow-bean-definition-overriding/exclude  bean");
        }
    }
}
