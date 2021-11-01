package help.lixin.route.parse.impl;

import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;
import help.lixin.route.model.RouteInfoList;
import help.lixin.route.parse.IRouteParseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RewriteRouteParseService implements IRouteParseService {

    private static Logger logger = LoggerFactory.getLogger(RewriteRouteParseService.class);

    @Override
    public RouteInfoList parse(String xroute) {
        RouteInfoList.Builder builder = RouteInfoList.newBuilder();
        // 路由重写: "test-provider/127.0.0.1:8080,test-provider2/127.0.0.1:8081"
        // 1. 按","分隔.
        String[] items = xroute.split(",");
        for (String item : items) {
            // 3. 按"/"分隔
            String[] ruleItems = item.split("/");
            if (ruleItems.length == 2) {
                String serviceId = ruleItems[0];
                String address = ruleItems[1];
                if (null != serviceId && null != address) {
                    IRouteInfo routeInfo = RouteInfo.newBuilder().serviceId(serviceId).address(address)
                            .build();
                    builder.addRouteInfo(routeInfo);
                } else {
                    logger.warn("ignore rule:[{}]", item);
                }
            } else {
                logger.warn("ignore rule:[{}]", item);
            }
        }
        return builder.build();
    }
}
