package help.lixin.route.eureka;

import com.netflix.appinfo.InstanceInfo;
import help.lixin.route.interceptor.IInstanceInterceptorFace;

import java.util.ArrayList;
import java.util.List;

public class EurekaIInstanceInterceptorFace implements IInstanceInterceptorFace<InstanceInfo> {

    private List<IEurekaIInstanceInterceptor> interceptors = new ArrayList<>();

    public void setInterceptors(List<IEurekaIInstanceInterceptor> interceptors) {
        if (null != interceptors) {
            this.interceptors = interceptors;
        }
    }

    public List<IEurekaIInstanceInterceptor> getInterceptors() {
        return interceptors;
    }

    public void filter(List<InstanceInfo> instances) {
        if (null == interceptors || interceptors.isEmpty()) {
            return;
        }
        if (null == instances || instances.isEmpty()) {
            return;
        }
        interceptors.forEach(interceptor -> interceptor.filter(instances));
    }
}
