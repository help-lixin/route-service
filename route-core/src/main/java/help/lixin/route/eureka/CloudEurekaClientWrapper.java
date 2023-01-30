package help.lixin.route.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.EurekaClientConfig;
import help.lixin.route.interceptor.IInstanceInterceptorFace;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;

public class CloudEurekaClientWrapper extends CloudEurekaClient {

    private IInstanceInterceptorFace<InstanceInfo> instanceInterceptorFace;

    public void setInstanceInterceptorFace(IInstanceInterceptorFace<InstanceInfo> instanceInterceptorFace) {
        this.instanceInterceptorFace = instanceInterceptorFace;
    }

    public IInstanceInterceptorFace<InstanceInfo> getInstanceInterceptorFace() {
        return instanceInterceptorFace;
    }

    public CloudEurekaClientWrapper(ApplicationInfoManager applicationInfoManager, EurekaClientConfig config, ApplicationEventPublisher publisher, IInstanceInterceptorFace<InstanceInfo> instanceInterceptorFace) {
        super(applicationInfoManager, config, publisher);
        this.instanceInterceptorFace = instanceInterceptorFace;
    }

    public CloudEurekaClientWrapper(ApplicationInfoManager applicationInfoManager, EurekaClientConfig config, AbstractDiscoveryClientOptionalArgs<?> args, ApplicationEventPublisher publisher, IInstanceInterceptorFace<InstanceInfo> IInstanceInterceptorFace) {
        super(applicationInfoManager, config, args, publisher);
        this.instanceInterceptorFace = IInstanceInterceptorFace;
    }

    @Override
    public List<InstanceInfo> getInstancesByVipAddress(String vipAddress, boolean secure, String region) {
        List<InstanceInfo> resultList = new ArrayList<>();
        List<InstanceInfo> instancesByVipAddress = super.getInstancesByVipAddress(vipAddress, secure, region);
        resultList.addAll(instancesByVipAddress);

        // 交给拦截器处理
        instanceInterceptorFace.filter(resultList);
        return resultList;
    }

    @Override
    public List<InstanceInfo> getInstancesByVipAddressAndAppName(String vipAddress, String appName, boolean secure) {
        List<InstanceInfo> resultList = new ArrayList<>();
        List<InstanceInfo> instancesByVipAddressAndAppName = super.getInstancesByVipAddressAndAppName(vipAddress, appName, secure);
        resultList.addAll(instancesByVipAddressAndAppName);

        // 交给拦截器处理
        instanceInterceptorFace.filter(resultList);
        return resultList;
    }
}
