package help.lixin.route.eureka;

import help.lixin.route.interceptor.IInstanceInterceptor;

import java.util.List;

/**
 * 对InstanceInfo进行过滤
 */
public interface IEurekaIInstanceInterceptor<InstanceInfo> extends IInstanceInterceptor<InstanceInfo> {
    @Override
    void filter(List<InstanceInfo> instances);
}
