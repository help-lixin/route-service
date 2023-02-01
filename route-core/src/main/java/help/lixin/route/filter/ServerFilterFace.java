package help.lixin.route.filter;

import com.netflix.loadbalancer.Server;
import help.lixin.route.core.meta.ctx.RouteInfoContext;

import java.util.ArrayList;
import java.util.List;

public class ServerFilterFace implements IServerFilterFace<Server> {

    private List<IServerFilter<Server>> filterList = new ArrayList<>();

    public void setFilterList(List<IServerFilter<Server>> filterList) {
        if (null != filterList) {
            this.filterList = filterList;
        }

    }

    public List<IServerFilter<Server>> getFilterList() {
        return filterList;
    }

    public void filter(RouteInfoContext ctx, List<Server> instances) {
        if (null == filterList || filterList.isEmpty()) {
            return;
        }
        if (null == instances || instances.isEmpty()) {
            return;
        }
        filterList.forEach(interceptor -> interceptor.filter(ctx,instances));
    }
}
