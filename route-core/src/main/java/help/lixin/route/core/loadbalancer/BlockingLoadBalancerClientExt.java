package help.lixin.route.core.loadbalancer;

import help.lixin.route.constants.Constants;
import help.lixin.route.core.parse.RouteParseServiceFace;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;
import help.lixin.route.model.RouteInfoList;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;

import java.util.List;

public class BlockingLoadBalancerClientExt extends BlockingLoadBalancerClient {
    private RouteParseServiceFace routeParseServiceFace;


    public BlockingLoadBalancerClientExt(LoadBalancerClientFactory loadBalancerClientFactory, LoadBalancerProperties properties, RouteParseServiceFace routeParseServiceFace) {
        super(loadBalancerClientFactory, properties);
        this.routeParseServiceFace = routeParseServiceFace;
    }

    @Override
    public <T> ServiceInstance choose(String serviceId, Request<T> request) {
        if (request instanceof DefaultRequest) {
            // 从请求头里,获得x-route信息
            DefaultRequest<RequestDataContext> defaultRequest = (DefaultRequest<RequestDataContext>) request;
            RequestData clientRequest = defaultRequest.getContext().getClientRequest();
            List<String> routeHeaderList = clientRequest.getHeaders().get(Constants.ROUTE_KEY);

            if (null != routeHeaderList && routeHeaderList.size() > 0) {
                String xroute = routeHeaderList.get(0);
                // 判断serviceId是否在x-route协议头里有配置,如果有配置,代表要做特殊处理.
                RouteInfoList routeInfoList = routeParseServiceFace.transform(xroute);
                if (null != routeInfoList) {
                    IRouteInfo routeInfo = routeInfoList.getRouteInfos().get(serviceId);
                    if (null != routeInfo && routeInfo instanceof RouteInfo) {
                        RouteInfo tmpRouteInfo = (RouteInfo) routeInfo;
                        // serviceId#host#port
                        String newServiceId = String.format(Constants.SERVICE_ID_FORMAT, serviceId, tmpRouteInfo.getIp(), tmpRouteInfo.getPort());
                        return super.choose(newServiceId, request);
                    }
                }
            }
        }
        return super.choose(serviceId, request);
    }
}
