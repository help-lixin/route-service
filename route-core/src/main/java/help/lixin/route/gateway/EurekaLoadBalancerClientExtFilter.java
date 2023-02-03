package help.lixin.route.gateway;

public class EurekaLoadBalancerClientExtFilter  {
//    extends LoadBalancerClientFilter {
//    private Logger logger = LoggerFactory.getLogger(EurekaLoadBalancerClientExtFilter.class);
//
//    private RouteParseServiceFace routeParseServiceFace;
//    private IServerFilterFace<Server> serverFilterFace;
//
//    private EurekaClient eurekaClient;
//
//    public void setEurekaClient(EurekaClient eurekaClient) {
//        this.eurekaClient = eurekaClient;
//    }
//
//    public EurekaClient getEurekaClient() {
//        return eurekaClient;
//    }
//
//
//    public void setServerFilterFace(IServerFilterFace<Server> serverFilterFace) {
//        this.serverFilterFace = serverFilterFace;
//    }
//
//    public IServerFilterFace<Server> getServerFilterFace() {
//        return serverFilterFace;
//    }
//
//    public EurekaLoadBalancerClientExtFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
//        super(loadBalancer, properties);
//    }
//
//    public void setRouteParseServiceFace(RouteParseServiceFace routeParseServiceFace) {
//        this.routeParseServiceFace = routeParseServiceFace;
//    }
//
//    public RouteParseServiceFace getRouteParseServiceFace() {
//        return routeParseServiceFace;
//    }
//
//    @Override
//    protected ServiceInstance choose(ServerWebExchange exchange) {
//        // 1. 获得要请求的微服务名称
//        String serviceId = ((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost().toLowerCase();
//        // 2. 获得协议头上的信息
//        String routeString = exchange.getRequest().getHeaders().getFirst(Constants.ROUTE_KEY);
//        if (null != routeString) {
//            // 转换字符串信息到业务模型,如果转换出现了问题,也不要影响业务正常流转.
//            RouteInfoList routeInfoCollection = null;
//            try {
//                // 3. 对协议头里的信息进行解析,转换成业务模型.
//                routeInfoCollection = routeParseServiceFace.transform(routeString);
//            } catch (Throwable e) {
//                logger.warn("parse route info error:[{}]", e);
//            }
//
//            if (null != routeInfoCollection && !routeInfoCollection.getRouteInfos().isEmpty()) {
//                IRouteInfo routeInfo = routeInfoCollection.getRouteInfos().get(serviceId);
//                if (null != routeInfo) {
//                    // 4. 构建路由信息的上下文.
//                    RouteInfoContext ctx = RouteInfoContext.newBuilder()
//                            //
//                            .routeInfo(routeInfo)
//                            //
//                            .build();
//                    // 通过Eureka拿出所有的微服务信息
//                    List<Server> tmpServer = transformToServer(eurekaClient.getInstancesByVipAddress(serviceId, false));
//                    // 5. 委托给路由门面进行处理.
//                    serverFilterFace.filter(ctx, tmpServer);
//                    // 6. 在这里只取一个出来用.
//                    // TODO lixin
//                    if (!tmpServer.isEmpty()) {
//                        Server server = tmpServer.get(0);
//                        ServiceInstance serviceInstance = new RibbonLoadBalancerClient.RibbonServer(serviceId, server);
//                        return serviceInstance;
//                    }
//                }
//            }
//        }
//        return super.choose(exchange);
//    }
//
//
//    protected List<Server> transformToServer(List<InstanceInfo> infos) {
//        List<Server> servers = new ArrayList<>();
//        if (null != infos && !infos.isEmpty()) {
//            for (InstanceInfo instance : infos) {
//                DiscoveryEnabledServer discoveryEnabledServer = new DiscoveryEnabledServer(instance, false);
//                servers.add(discoveryEnabledServer);
//            }
//        }
//        return servers;
//    }
}