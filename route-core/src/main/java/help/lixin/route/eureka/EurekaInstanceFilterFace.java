package help.lixin.route.eureka;

import com.netflix.appinfo.InstanceInfo;
import help.lixin.route.filter.IInstanceFilterFace;
import help.lixin.route.core.meta.ctx.RouteInfoContext;

import java.util.ArrayList;
import java.util.List;

public class EurekaInstanceFilterFace implements IInstanceFilterFace<InstanceInfo> {

    private List<IEurekaInstanceFilter> interceptors = new ArrayList<>();

    public void setInterceptors(List<IEurekaInstanceFilter> interceptors) {
        if (null != interceptors) {
            this.interceptors = interceptors;
        }
    }

    public List<IEurekaInstanceFilter> getInterceptors() {
        return interceptors;
    }

    public void filter(RouteInfoContext ctx, List<InstanceInfo> instances) {
        if (null == interceptors || interceptors.isEmpty()) {
            return;
        }
        if (null == instances || instances.isEmpty()) {
            return;
        }
        interceptors.forEach(interceptor -> interceptor.filter(ctx, instances));
    }
}
