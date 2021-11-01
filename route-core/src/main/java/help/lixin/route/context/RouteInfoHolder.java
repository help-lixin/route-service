package help.lixin.route.context;


import help.lixin.route.model.RouteInfoList;

public abstract class RouteInfoHolder {
    protected static final ThreadLocal<RouteInfoList> ROUTE_CONTEXT = new InheritableThreadLocal<>();

    public static void set(RouteInfoList routeInfoList) {
        ROUTE_CONTEXT.set(routeInfoList);
    }

    public static RouteInfoList get() {
        return ROUTE_CONTEXT.get();
    }

    public static boolean isEnabled() {
        RouteInfoList routeInfoCollection = get();
        if (null != routeInfoCollection && !routeInfoCollection.getRouteInfos().isEmpty()) {
            return true;
        }
        return false;
    }

    public static void clean() {
        ROUTE_CONTEXT.remove();
    }
}
