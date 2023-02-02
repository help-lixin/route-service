package help.lixin.route;

import help.lixin.route.config.*;
import help.lixin.route.constants.Constants;
import help.lixin.route.properties.RouteProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration({EurekaRewriteServerConfig.class, NacosRewriteServerConfig.class, CommonRouteConfig.class, FeignRouteConfig.class, WebRouteConfig.class, EurekaGatewayRouteConfig.class, NacosGatewayRouteConfig.class})
@EnableConfigurationProperties(RouteProperties.class)
@ConditionalOnProperty(prefix = Constants.ROUTE_KEY, name = Constants.ENABLED, havingValue = "true", matchIfMissing = false)
public class RouteAutoConfiguration {
    private Logger logger = LoggerFactory.getLogger(RouteAutoConfiguration.class);

    private RouteProperties routeProperties;

    public RouteAutoConfiguration(RouteProperties routeProperties) {
        this.routeProperties = routeProperties;
    }

    {
        if (logger.isDebugEnabled()) {
            logger.debug("enabled Module [{}] SUCCESS.", RouteAutoConfiguration.class.getSimpleName());
        }
    }
}
