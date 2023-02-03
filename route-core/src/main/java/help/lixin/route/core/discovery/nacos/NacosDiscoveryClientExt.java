package help.lixin.route.core.discovery.nacos;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import help.lixin.route.model.RouteInfo;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.List;

public class NacosDiscoveryClientExt extends NacosDiscoveryClient {
    private IServiceInstanceFilterFace serviceInstanceFilterFace;


    public NacosDiscoveryClientExt(NacosServiceDiscovery nacosServiceDiscovery, IServiceInstanceFilterFace serviceInstanceFilterFace) {
        super(nacosServiceDiscovery);
        this.serviceInstanceFilterFace = serviceInstanceFilterFace;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceIdCtx) {
        String serviceId = serviceIdCtx;
        String ip = null;
        Integer port = null;
        boolean isEnabledInstanceFilter = Boolean.FALSE;

        String[] serviceContextArray = serviceIdCtx.split("#");
        if (serviceContextArray.length == 3) {
            serviceId = serviceContextArray[0];
            ip = serviceContextArray[1];
            port = Integer.parseInt(serviceContextArray[2]);
            isEnabledInstanceFilter = Boolean.TRUE;
        }

        // 1. 正常去取上下文信息
        List<ServiceInstance> instanceList = super.getInstances(serviceId);

        List<ServiceInstance> copyServiceInstances = new ArrayList<>();
        copyServiceInstances.addAll(instanceList);

        if (isEnabledInstanceFilter) {
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
        }
        return copyServiceInstances;
    }
}