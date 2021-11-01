package org.springframework.cloud.netflix.ribbon.eureka;

import java.io.IOException;
import java.net.URI;

import help.lixin.route.context.RouteInfoHolder;
import help.lixin.route.meta.RouteServiceFace;
import help.lixin.route.meta.ctx.RouteInfoContext;
import help.lixin.route.model.IRouteInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonProperties;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.client.ClientException;
import com.netflix.client.RetryHandler;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

/**
 * 由于DomainExtractingServer的包名称是默认的,所以,这个类只能放在这个包下:org.springframework.cloud.netflix.ribbon.eureka
 * 
 * @author lixin
 *
 */
public class FeignLoadBalancerExt extends FeignLoadBalancer {

	private Logger logger = LoggerFactory.getLogger(FeignLoadBalancerExt.class);

	private LoadBalancerClient loadBalancerClient;
	private RouteServiceFace routeServiceFace;

	private final RibbonProperties ribbon;
	protected int connectTimeout;
	protected int readTimeout;
	protected IClientConfig clientConfig;
	protected ServerIntrospector serverIntrospector;

	public FeignLoadBalancerExt(ILoadBalancer lb, IClientConfig clientConfig, ServerIntrospector serverIntrospector) {
		super(lb, clientConfig, serverIntrospector);
		this.setRetryHandler(RetryHandler.DEFAULT);
		this.clientConfig = clientConfig;
		this.ribbon = RibbonProperties.from(clientConfig);
		RibbonProperties ribbon = this.ribbon;
		this.connectTimeout = ribbon.getConnectTimeout();
		this.readTimeout = ribbon.getReadTimeout();
		this.serverIntrospector = serverIntrospector;
	}

	public void setLoadBalancerClient(LoadBalancerClient loadBalancerClient) {
		this.loadBalancerClient = loadBalancerClient;
	}

	public LoadBalancerClient getLoadBalancerClient() {
		return loadBalancerClient;
	}

	public void setRouteServiceFace(RouteServiceFace routeServiceFace) {
		this.routeServiceFace = routeServiceFace;
	}

	public RouteServiceFace getRouteServiceFace() {
		return routeServiceFace;
	}

	@Override
	public RibbonResponse executeWithLoadBalancer(RibbonRequest request, IClientConfig requestConfig)
			throws ClientException {
		// 1. 判断是否启用了路由
		if (RouteInfoHolder.isEnabled()) {
			String serviceId = requestConfig.getClientName();
			IRouteInfo routeInfo = RouteInfoHolder.get().getRouteInfos().get(serviceId);
			if (null != routeInfo) {
				if (logger.isDebugEnabled()) {
					logger.debug("proxy micro service name [{}] to rule [{}]", serviceId, routeInfo);
				}
				// 构建:RouteInfoContext
				RouteInfoContext ctx = RouteInfoContext.newBuilder().routeInfo(routeInfo).build();
				ServiceInstance serviceInstance = routeServiceFace.getServiceInstance(ctx);
				if (null != serviceInstance) {
					Server server = this.buildServer(serviceInstance);
					URI finalUri = reconstructURIWithServer(server, request.getUri());
					RibbonRequest requestForServer = (RibbonRequest) request
							.replaceUri(finalUri);
					try {
						return execute(requestForServer, requestConfig);
					} catch (IOException ignore) {
						logger.warn("request serviceId:[{}] error,Exception:[{}]", serviceId, ignore);
					}
				}
			}
		}
		
		return super.executeWithLoadBalancer(request, requestConfig);
	}

	public Server buildServer(ServiceInstance serviceInstance) {
		String ipAddr = serviceInstance.getHost() + ":" + serviceInstance.getPort();
		InstanceInfo instanceInfo = InstanceInfo.Builder.newBuilder().setPort(serviceInstance.getPort())
				.setHostName(serviceInstance.getHost()).setIPAddr(ipAddr).setAppName(serviceInstance.getServiceId())
				.build();
		DiscoveryEnabledServer discoveryEnabledServer = new DiscoveryEnabledServer(instanceInfo, false, false);
		DomainExtractingServer domainExtractingServer = new DomainExtractingServer(discoveryEnabledServer, false, false,
				false);
		return domainExtractingServer;
	}
}
