package help.lixin.route.filter;

import help.lixin.route.core.meta.ctx.RouteInfoContext;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.List;

public class ServiceInstanceFilterFace implements IServiceInstanceFilterFace<ServiceInstance> {

    private List<IServiceInstanceFilter<ServiceInstance>> filterList = new ArrayList<>();

    public void setFilterList(List<IServiceInstanceFilter<ServiceInstance>> filterList) {
        if (null != filterList) {
            this.filterList = filterList;
        }

    }

    public List<IServiceInstanceFilter<ServiceInstance>> getFilterList() {
        return filterList;
    }

    public void filter(RouteInfoContext ctx, List<ServiceInstance> instances) {
        if (null == filterList || filterList.isEmpty()) {
            return;
        }
        filterList.forEach(interceptor -> interceptor.filter(ctx, instances));
    }
}
