package help.lixin.route.core.parse;

import java.util.ArrayList;
import java.util.List;

import help.lixin.route.model.RouteInfoList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouteParseServiceFace {

    private static Logger logger = LoggerFactory.getLogger(RouteParseServiceFace.class);

    private List<IRouteParseService> chains = new ArrayList<IRouteParseService>();

    public void setChains(List<IRouteParseService> chains) {
        this.chains = chains;
    }

    public List<IRouteParseService> getChains() {
        return chains;
    }

    public RouteInfoList transform(String xroute) {
        for (IRouteParseService routeParseService : chains) {
            RouteInfoList res = routeParseService.parse(xroute);
            if (!res.getRouteInfos().isEmpty()) {
                return res;
            }
        }
        logger.debug("x-route:{},not found IRouteParseService impl", xroute);
        return null;
    }
}
