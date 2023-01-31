package help.lixin.route.eureka.impl;

import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInfo;
import help.lixin.route.eureka.IEurekaInstanceFilter;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;

import java.util.List;

public class RewriteEurekaRouteFilter implements IEurekaInstanceFilter<InstanceInfo> {
    @Override
    public void filter(RouteInfoContext ctx, List<InstanceInfo> instances) {
        IRouteInfo routeInfo = ctx.getRouteInfo();
        if (routeInfo instanceof RouteInfo) {
            String serviceId = routeInfo.getServiceId();
            RouteInfo rewriteRouteInfo = (RouteInfo) routeInfo;
            String instanceId = String.format("%s:%s:%s:%s", "mock", rewriteRouteInfo.getIp(), serviceId, rewriteRouteInfo.getPort());

            // 1. mock出一个实例出来
            InstanceInfo mockInstanceInfo = InstanceInfo.Builder.newBuilder()
                    //
                    .setInstanceId(instanceId)
                    //
                    .setAppName(serviceId)
                    //
                    .setDataCenterInfo(new MyDataCenterInfo(DataCenterInfo.Name.MyOwn))
                    //
                    .setHostName(rewriteRouteInfo.getIp())
                    //
                    .setIPAddr(rewriteRouteInfo.getIp())
                    //
                    .setPort(rewriteRouteInfo.getPort())
                    //
                    .build();
            // 2. 清空所有实例
            instances.clear();
            // 3. 重新添加到集合中
            instances.add(mockInstanceInfo);
        }
    }
}
