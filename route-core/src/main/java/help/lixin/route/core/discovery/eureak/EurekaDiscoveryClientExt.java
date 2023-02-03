package help.lixin.route.core.discovery.eureak;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;

import java.util.ArrayList;
import java.util.List;

public class EurekaDiscoveryClientExt extends EurekaDiscoveryClient {

    private IServiceInstanceFilterFace serviceInstanceFilterFace;

    public EurekaDiscoveryClientExt(EurekaClient eurekaClient, EurekaClientConfig clientConfig, IServiceInstanceFilterFace serviceInstanceFilterFace) {
        super(eurekaClient, clientConfig);
        this.serviceInstanceFilterFace = serviceInstanceFilterFace;
    }

    public void setServiceInstanceFilterFace(IServiceInstanceFilterFace serviceInstanceFilterFace) {
        this.serviceInstanceFilterFace = serviceInstanceFilterFace;
    }

    public IServiceInstanceFilterFace getServiceInstanceFilterFace() {
        return serviceInstanceFilterFace;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceIdCtx) {
        String serviceId = serviceIdCtx;
        String ip = null;
        Integer port = null;

        String[] serviceContextArray = serviceIdCtx.split("#");
        if (serviceContextArray.length == 3) {
            serviceId = serviceContextArray[0];
            ip = serviceContextArray[1];
            port = Integer.parseInt(serviceContextArray[2]);
        }

        // 正常去取上下文信息
        List<ServiceInstance> instanceList = super.getInstances(serviceId);

        List<ServiceInstance> copyServiceInstances = new ArrayList<>();
        copyServiceInstances.addAll(instanceList);

        RouteInfo routeInfo = RouteInfo.newBuilder()
                //
                .serviceId(serviceId)
                //
                .ip(ip)
                //
                .port(port)
                //
                .build();
        RouteInfoContext ctx = RouteInfoContext.newBuilder()
                //
                .routeInfo(routeInfo)
                //
                .other(Constants.SERVICE_ID, serviceId)
                //
                .build();
        serviceInstanceFilterFace.filter(ctx, copyServiceInstances);
        return copyServiceInstances;
    }
}
