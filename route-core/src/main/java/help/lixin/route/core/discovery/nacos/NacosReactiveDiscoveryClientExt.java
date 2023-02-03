package help.lixin.route.core.discovery.nacos;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.cloud.nacos.discovery.reactive.NacosReactiveDiscoveryClient;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import help.lixin.route.model.RouteInfo;
import org.springframework.cloud.client.ServiceInstance;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class NacosReactiveDiscoveryClientExt extends NacosReactiveDiscoveryClient {

    private IServiceInstanceFilterFace serviceInstanceFilterFace;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public NacosReactiveDiscoveryClientExt(NacosServiceDiscovery nacosServiceDiscovery, IServiceInstanceFilterFace serviceInstanceFilterFace) {
        super(nacosServiceDiscovery);
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

        // all
        Flux<ServiceInstance> instances = super.getInstances(serviceId);
        Future<List<ServiceInstance>> listFuture = executorService.submit(new Callable<List<ServiceInstance>>() {
            @Override
            public List<ServiceInstance> call() throws Exception {
                List<ServiceInstance> all = instances.collectList().block();

                List<ServiceInstance> copyServiceInstances = new ArrayList<>();
                copyServiceInstances.addAll(all);
                return copyServiceInstances;
            }
        });

        try {
            List<ServiceInstance> copyServiceInstances = listFuture.get();

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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
