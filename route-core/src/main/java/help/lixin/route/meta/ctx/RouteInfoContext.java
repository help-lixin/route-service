package help.lixin.route.meta.ctx;

import java.util.HashMap;
import java.util.Map;

import help.lixin.route.model.IRouteInfo;

/**
 * 通过RouteInfoContext承载所有的数据,主要是为了开发以后可以自由扩展.
 *
 * @author lixin
 */
public class RouteInfoContext {
    private IRouteInfo routeInfo;
    private Map<String, Object> others = new HashMap<String, Object>();

    public IRouteInfo getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(IRouteInfo routeInfo) {
        this.routeInfo = routeInfo;
    }

    public Map<String, Object> getOthers() {
        return others;
    }

    public void setOthers(Map<String, Object> others) {
        this.others = others;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private IRouteInfo routeInfo;
        private Map<String, Object> others = new HashMap<String, Object>();

        public Builder routeInfo(IRouteInfo routeInfo) {
            this.routeInfo = routeInfo;
            return this;
        }

        public Builder others(Map<String, Object> others) {
            if (null != others) {
                this.others = others;
            }
            return this;
        }

        public Builder other(String key, Object value) {
            others.put(key, value);
            return this;
        }

        public RouteInfoContext build() {
            assert null != routeInfo;
            RouteInfoContext ctx = new RouteInfoContext();
            ctx.setRouteInfo(routeInfo);
            ctx.setOthers(others);
            return ctx;
        }
    }
}
