package help.lixin.route.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 承载着所有的路由载体
 */
public class RouteInfoList {
    private Map<String, IRouteInfo> routeInfos = new HashMap<>();

    public Map<String, IRouteInfo> getRouteInfos() {
        return routeInfos;
    }

    public void setRouteInfos(Map<String, IRouteInfo> routeInfos) {
        if (null != routeInfos) {
            this.routeInfos = routeInfos;
        }
    }

    public boolean isExists(String serviceId) {
        return routeInfos.containsKey(serviceId);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Map<String, IRouteInfo> routeInfos = new HashMap<>();

        public Builder addRouteInfo(IRouteInfo routeInfo) {
            if (null != routeInfo) {
                routeInfos.put(routeInfo.getServiceId(), routeInfo);
            }
            return this;
        }

        public Builder addRouteInfos(Map<String, IRouteInfo> routeInfos) {
            if (null != routeInfos) {
                routeInfos.putAll(routeInfos);
            }
            return this;
        }

        public RouteInfoList build() {
            RouteInfoList routeInfoList = new RouteInfoList();
            routeInfoList.setRouteInfos(this.routeInfos);
            return routeInfoList;
        }
    }

    @Override
    public String toString() {
        return "RouteInfoList{" +
                "routeInfos=" + routeInfos +
                '}';
    }
}
