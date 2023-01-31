package help.lixin.route.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.EurekaClientConfig;
import help.lixin.route.core.context.RouteInfoHolder;
import help.lixin.route.filter.IInstanceFilterFace;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.model.IRouteInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudEurekaClientWrapper extends CloudEurekaClient {
    private Logger logger = LoggerFactory.getLogger(CloudEurekaClientWrapper.class);

    private IInstanceFilterFace<InstanceInfo> instanceFilterFace;

    public void setInstanceFilterFace(IInstanceFilterFace<InstanceInfo> instanceFilterFace) {
        this.instanceFilterFace = instanceFilterFace;
    }

    public IInstanceFilterFace<InstanceInfo> getInstanceFilterFace() {
        return instanceFilterFace;
    }

    public CloudEurekaClientWrapper(ApplicationInfoManager applicationInfoManager, EurekaClientConfig config, ApplicationEventPublisher publisher, IInstanceFilterFace<InstanceInfo> instanceFilterFace) {
        super(applicationInfoManager, config, publisher);
        this.instanceFilterFace = instanceFilterFace;
    }

    public CloudEurekaClientWrapper(ApplicationInfoManager applicationInfoManager, EurekaClientConfig config, AbstractDiscoveryClientOptionalArgs<?> args, ApplicationEventPublisher publisher, IInstanceFilterFace<InstanceInfo> instanceFilterFace) {
        super(applicationInfoManager, config, args, publisher);
        this.instanceFilterFace = instanceFilterFace;
    }

    @Override
    public List<InstanceInfo> getInstancesByVipAddress(String vipAddress, boolean secure, String region) {
        List<InstanceInfo> instancesByVipAddress = super.getInstancesByVipAddress(vipAddress, secure, region);

        Map<String, Object> params = new HashMap<>();
        params.put("vipAddress", vipAddress);
        params.put("secure", secure);
        params.put("region", region);

        return process(params, instancesByVipAddress);
    }

    @Override
    public List<InstanceInfo> getInstancesByVipAddressAndAppName(String vipAddress, String appName, boolean secure) {
        List<InstanceInfo> instancesByVipAddressAndAppName = super.getInstancesByVipAddressAndAppName(vipAddress, appName, secure);

        Map<String, Object> params = new HashMap<>();
        params.put("vipAddress", vipAddress);
        params.put("secure", secure);
        params.put("appName", appName);

        return process(params, instancesByVipAddressAndAppName);
    }

    protected List<InstanceInfo> process(Map<String, Object> params, List<InstanceInfo> eurekaInstances) {
        List<InstanceInfo> lastInstances = new ArrayList<>();
        if (null != eurekaInstances) {
            lastInstances.addAll(eurekaInstances);
        }

        String vipAddress = (String) params.get("vipAddress");
        // 开启了路由功能
        if (RouteInfoHolder.isEnabled()) {
            // 路由处理
            IRouteInfo routeInfo = RouteInfoHolder.get().getRouteInfos().get(vipAddress);
            if (null != routeInfo) {
                if (logger.isDebugEnabled()) {
                    logger.debug("proxy micro service name [{}] to rule [{}]", vipAddress, routeInfo);
                }

                // 构建:RouteInfoContext,把上下文信息带过去.
                RouteInfoContext ctx = RouteInfoContext.newBuilder()
                        //
                        .routeInfo(routeInfo)
                        //
                        .others(params)
                        //
                        .other("original", eurekaInstances)
                        .build();
                // 交给拦截器处理
                instanceFilterFace.filter(ctx, lastInstances);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("serviceName:[{}],routes:[{}]", vipAddress, lastInstances);
        }
        return lastInstances;
    }

}
