package help.lixin.route.core.discovery.eureak;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import help.lixin.route.model.RouteInfo;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.reactive.EurekaReactiveDiscoveryClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class EurekaReactiveDiscoveryClientExt extends EurekaReactiveDiscoveryClient {
    private IServiceInstanceFilterFace serviceInstanceFilterFace;

    public EurekaReactiveDiscoveryClientExt(EurekaClient eurekaClient, EurekaClientConfig clientConfig, IServiceInstanceFilterFace serviceInstanceFilterFace) {
        super(eurekaClient, clientConfig);
        this.serviceInstanceFilterFace = serviceInstanceFilterFace;
    }

    @Override
    public Flux<ServiceInstance> getInstances(String serviceIdCtx) {
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

        Flux<ServiceInstance> instances = super.getInstances(serviceId);
        List<ServiceInstance> copyServiceInstances = new ArrayList<>();
        instances.subscribe(i -> {
            copyServiceInstances.add(i);
        });

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
        return Flux.fromIterable(copyServiceInstances);
    }
}
