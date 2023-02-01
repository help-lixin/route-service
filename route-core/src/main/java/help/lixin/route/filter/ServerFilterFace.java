package help.lixin.route.filter;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.Server;
import help.lixin.route.core.meta.ctx.RouteInfoContext;

import java.util.ArrayList;
import java.util.List;

public class EurekaServerFilterFace implements IServerFilterFace<Server> {

    private List<IServerFilter> serverFilters = new ArrayList<>();

    public void setServerFilters(List<IServerFilter> serverFilters) {
        if (null != serverFilters) {
            this.serverFilters = serverFilters;
        }
    }

    public List<IServerFilter> getServerFilters() {
        return serverFilters;
    }

    public void filter(List<Server> instances) {
        if (null == serverFilters || serverFilters.isEmpty()) {
            return;
        }
        if (null == instances || instances.isEmpty()) {
            return;
        }
        serverFilters.forEach(interceptor -> interceptor.filter(instances));
    }
}
