package help.lixin.route.transmit.rest;

import java.io.IOException;

import help.lixin.route.constants.Constants;
import help.lixin.route.core.context.XRouteHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * 把线程上下文(XRouteHolder),数据取出来,继续传递
 */
public class RouteRequestInterceptor implements ClientHttpRequestInterceptor {
    private Logger logger = LoggerFactory.getLogger(RouteRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (XRouteHolder.isEnabled()) {
            String xrouteValue = XRouteHolder.get();
            request.getHeaders().add(Constants.ROUTE_KEY, xrouteValue);
            if (logger.isDebugEnabled()) {
                logger.debug("add [{}:{}] to request header", Constants.ROUTE_KEY, xrouteValue);
            }
        }
        return execution.execute(request, body);
    }
}
