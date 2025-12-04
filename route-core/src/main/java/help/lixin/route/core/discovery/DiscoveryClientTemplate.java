package help.lixin.route.core.discovery;

import help.lixin.route.constants.Constants;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.core.serviceid.IServiceIdService;
import help.lixin.route.core.serviceid.model.IServiceId;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import help.lixin.route.model.RouteInfo;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryClientTemplate implements IDiscoveryClientTemplate {

    protected IServiceInstanceFilterFace serviceInstanceFilterFace;

    protected IServiceIdService serviceIdService;

    public DiscoveryClientTemplate(IServiceInstanceFilterFace serviceInstanceFilterFace, IServiceIdService serviceIdService) {
        this.serviceInstanceFilterFace = serviceInstanceFilterFace;
        this.serviceIdService = serviceIdService;
    }

    public List<ServiceInstance> getInstances(String serviceIdCtx, ServiceInstanceCallback callback) {
        String serviceId = serviceIdCtx;
        String group = null;
        String ip = null;
        Integer port = null;

        // 交给IServiceId解析与处理.
        IServiceId serviceIdModel = serviceIdService.decode(serviceId);
        if (null != serviceIdModel) {
            group = serviceIdModel.getGroup();
            serviceId = serviceIdModel.getServiceId();
            ip = serviceIdModel.getIp();
            port = serviceIdModel.getPort();
        }

        List<ServiceInstance> instanceList = callback.getInstanceList(serviceId);
        List<ServiceInstance> copyServiceInstances = new ArrayList<>();
        copyServiceInstances.addAll(instanceList);

        // 开发自己要检查ctx
        RouteInfoContext ctx = null;
        // ip和port存在的情况下,代表着要进行路由重新,才会把要重写的路由信息写入上下文里.
        if (null != ip && null != port) {
            RouteInfo routeInfo = RouteInfo.newBuilder().serviceId(serviceId).ip(ip).port(port).build();
            ctx = RouteInfoContext.newBuilder()
                    //
                    .routeInfo(routeInfo)
                    //
                    .other(Constants.SERVICE_ID, serviceId)
                    //
                    .build();
        }
        serviceInstanceFilterFace.filter(ctx, copyServiceInstances);
        return copyServiceInstances;
    }
}
