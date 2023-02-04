package help.lixin.route.constants;

public class Constants {
    public static final String ROUTE_KEY = "x-route";
    public static final String ROUTE_SPLIT = ".";
    public static final String ENABLED = "enabled";
    public static final String CONCAT_ROUTE_ENABLED = ROUTE_KEY + ROUTE_SPLIT + "enabled";

    public static final String SERVICE_ID = "serviceId";

    // group#serviceId#127.0.0.1#8080
    public static final String SERVICE_ID_FORMAT = "%s#%s#%s#%s";

    public static final String DEFAULT_GROUP = "default";
}
