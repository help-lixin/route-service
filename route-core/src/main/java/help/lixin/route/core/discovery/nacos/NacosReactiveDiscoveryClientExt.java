package help.lixin.route.core.discovery.nacos;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.cloud.nacos.discovery.reactive.NacosReactiveDiscoveryClient;
import help.lixin.route.constants.Constants;
import help.lixin.route.core.discovery.DiscoveryClientTemplate;
import help.lixin.route.core.discovery.IDiscoveryClientTemplate;
import help.lixin.route.core.discovery.ServiceInstanceCallback;
import help.lixin.route.core.discovery.polaris.PolarisReactiveDiscoveryClientExt;
import help.lixin.route.core.meta.ctx.RouteInfoContext;
import help.lixin.route.filter.IServiceInstanceFilterFace;
import help.lixin.route.model.RouteInfo;
import org.springframework.cloud.client.ServiceInstance;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class NacosReactiveDiscoveryClientExt extends NacosReactiveDiscoveryClient {

    private IDiscoveryClientTemplate discoveryClientTemplate;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public NacosReactiveDiscoveryClientExt(NacosServiceDiscovery nacosServiceDiscovery, IDiscoveryClientTemplate discoveryClientTemplate) {
        super(nacosServiceDiscovery);
        this.discoveryClientTemplate = discoveryClientTemplate;
    }

    @Override
    public Flux<ServiceInstance> getInstances(String serviceIdCtx) {
        return Flux.fromIterable(discoveryClientTemplate.getInstances(serviceIdCtx, new ServiceInstanceCallback() {
            @Override
            public List<ServiceInstance> getInstanceList(String serviceId) {
                List<ServiceInstance> res = new ArrayList<>();
                // 调用父类去拿.
                Flux<ServiceInstance> instances = NacosReactiveDiscoveryClientExt.super.getInstances(serviceId);
                // 通过异步处理
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
                    List<ServiceInstance> tmpServiceInstances = listFuture.get();
                    res.addAll(tmpServiceInstances);
                } catch (Exception ignore) {
                }
                return res;
            }
        }));
    }
}
