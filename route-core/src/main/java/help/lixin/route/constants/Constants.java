package help.lixin.route.constants;

public class Constants {
    public static final String ROUTE_KEY = "x-route";
    public static final String ROUTE_SPLIT = ".";
    public static final String ENABLED = "enabled";
    public static final String CONCAT_ROUTE_ENABLED = ROUTE_KEY + ROUTE_SPLIT + "enabled";

    public static final String DISCOVERY_TYPE = "_discovery";
    public static final String DISCOVERY_NACOS = "_nacos";
    public static final String DISCOVERY_EUREKA = "eureka";

    public static final String NACOS_RIBBON_CLIENT_CLASS = "help.lixin.route.ribbon.config.NacosRibbonClientConfig";
    public static final String EUREKA_RIBBON_CLIENT_CLASS = "help.lixin.route.ribbon.config.EurekaRibbonClientConfig";
}
