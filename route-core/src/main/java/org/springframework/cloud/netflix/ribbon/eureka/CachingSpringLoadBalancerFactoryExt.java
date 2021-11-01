package org.springframework.cloud.netflix.ribbon.eureka;

import java.util.Map;

import help.lixin.route.meta.RouteServiceFace;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;
import org.springframework.cloud.openfeign.ribbon.RetryableFeignLoadBalancer;
import org.springframework.util.ConcurrentReferenceHashMap;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;

public class CachingSpringLoadBalancerFactoryExt extends CachingSpringLoadBalancerFactory {

	private final SpringClientFactory factory;
	private LoadBalancedRetryFactory loadBalancedRetryFactory = null;

	private volatile Map<String, FeignLoadBalancer> cache = new ConcurrentReferenceHashMap<>();

	public CachingSpringLoadBalancerFactoryExt(SpringClientFactory factory) {
		super(factory);
		this.factory = factory;
	}

	public CachingSpringLoadBalancerFactoryExt(SpringClientFactory factory,
			LoadBalancedRetryFactory loadBalancedRetryPolicyFactory) {
		super(factory, loadBalancedRetryPolicyFactory);
		this.factory = factory;
		this.loadBalancedRetryFactory = loadBalancedRetryPolicyFactory;
	}

	@Override
	public FeignLoadBalancer create(String clientName) {
		FeignLoadBalancer client = this.cache.get(clientName);
		if (client != null) {
			return client;
		}
		
		IClientConfig config = this.factory.getClientConfig(clientName);
		ILoadBalancer lb = this.factory.getLoadBalancer(clientName);
		ServerIntrospector serverIntrospector = this.factory.getInstance(clientName, ServerIntrospector.class);
		LoadBalancerClient loadBalancerClient = factory.getInstance("loadBalancerClient", LoadBalancerClient.class);
		RouteServiceFace routeServiceFace = factory.getInstance("routeServiceFace", RouteServiceFace.class);
		
//		client = loadBalancedRetryFactory != null
//				? new RetryableFeignLoadBalancer(lb, config, serverIntrospector, loadBalancedRetryFactory)
//				: new FeignLoadBalancer(lb, config, serverIntrospector);

		client = loadBalancedRetryFactory != null
				? new RetryableFeignLoadBalancer(lb, config, serverIntrospector, loadBalancedRetryFactory)
				: new FeignLoadBalancerExt(lb, config, serverIntrospector);
		if(client instanceof FeignLoadBalancerExt) {
			((FeignLoadBalancerExt)client).setLoadBalancerClient(loadBalancerClient);
			((FeignLoadBalancerExt)client).setRouteServiceFace(routeServiceFace);
		}
		this.cache.put(clientName, client);
		return client;
	}
}
