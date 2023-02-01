package help.lixin.route.eureka.impl;

import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInfo;
import com.netflix.loadbalancer.Server;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServerFilter;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;

import java.util.List;

public class RewriteEurekaRouteFilter implements IServerFilter<Server> {
    @Override
    public void filter(List<Server> instances) {
        
    }
}
