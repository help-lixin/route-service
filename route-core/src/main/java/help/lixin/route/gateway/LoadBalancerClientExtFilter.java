package help.lixin.route.gateway;

import com.netflix.loadbalancer.Server;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.core.parse.RouteParseServiceFace;
import help.lixin.route.filter.IServerFilterFace;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfoList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.web.server.ServerWebExchange;

public class EurekaLoadBalancerClientExtFilter extends LoadBalancerClientFilter {
    private Logger logger = LoggerFactory.getLogger(EurekaLoadBalancerClientExtFilter.class);

    private RouteParseServiceFace routeParseServiceFace;
    private IServerFilterFace<Server> serverFilterFace;

    private EurekaClient eurekaClient;

    public void setEurekaClient(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    public EurekaClient getEurekaClient() {
        return eurekaClient;
    }


    public void setServerFilterFace(IServerFilterFace<Server> serverFilterFace) {
        this.serverFilterFace = serverFilterFace;
    }

    public IServerFilterFace<Server> getServerFilterFace() {
        return serverFilterFace;
    }

    public EurekaLoadBalancerClientExtFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
        super(loadBalancer, properties);
    }

    public void setRouteParseServiceFace(RouteParseServiceFace routeParseServiceFace) {
        this.routeParseServiceFace = routeParseServiceFace;
    }

    public RouteParseServiceFace getRouteParseServiceFace() {
        return routeParseServiceFace;
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        // 1. 获得要请求的微服务名称
        String serviceId = ((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost().toLowerCase();
        // 2. 获得协议头上的信息
        String routeString = exchange.getRequest().getHeaders().getFirst(Constants.ROUTE_KEY);
        if (null != routeString) {
            // 转换字符串信息到业务模型,如果转换出现了问题,也不要影响业务正常流转.
            RouteInfoList routeInfoCollection = null;
            try {
                // 3. 对协议头里的信息进行解析,转换成业务模型.
                routeInfoCollection = routeParseServiceFace.transform(routeString);
            } catch (Throwable e) {
                logger.warn("parse route info error:[{}]", e);
            }

            if (null != routeInfoCollection && !routeInfoCollection.getRouteInfos().isEmpty()) {
                IRouteInfo routeInfo = routeInfoCollection.getRouteInfos().get(serviceId);
                if (null != routeInfo) {
                    // 4. 构建路由信息的上下文.
                    RouteInfoContext ctx = RouteInfoContext.newBuilder().routeInfo(routeInfo).build();
                    List<InstanceInfo> instanceInfos = eurekaClient.getInstancesByVipAddress(serviceId, false);

                    

                    // ***************************************************************************************
                    // TODO lixin
                    // ***************************************************************************************
                    // 5. 委托给路由门面进行处理.
//                    serverFilterFace.filter(ctx, instanceInfos);
//                    List<ServiceInstance> tmpInstances = transform(serviceId, instanceInfos);
//                    if (!tmpInstances.isEmpty()) {
//                        return tmpInstances.get(0);
//                    }
                }
            }
        }
        return super.choose(exchange);
    }

    protected List<ServiceInstance> transform(String serviceId, List<InstanceInfo> instanceInfos) {
        List<ServiceInstance> instances = new ArrayList<>();
        if (null != instanceInfos && !instanceInfos.isEmpty()) {
            InstanceInfo instanceInfo = instanceInfos.get(0);
            DiscoveryEnabledServer discoveryEnabledServer = new DiscoveryEnabledServer(instanceInfo, false);
            RibbonLoadBalancerClient.RibbonServer ribbonServer = new RibbonLoadBalancerClient.RibbonServer(serviceId, discoveryEnabledServer, false, null);
            instances.add(ribbonServer);
        }
        return instances;
    }

}