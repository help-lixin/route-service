package help.lixin.route.core.meta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public class RouteServiceMediator {

	private final Map<Class<?>, IRouteService> ROUTE_SERVICES = new ConcurrentHashMap<Class<?>, IRouteService>(16);

	private RouteServiceMediator() {
	}

	public static class RouteServiceMediatorHolder {
		private static final RouteServiceMediator ROUTE_SERVICE_MEDIATOR = new RouteServiceMediator();
	}

	public static RouteServiceMediator getInstance() {
		return RouteServiceMediatorHolder.ROUTE_SERVICE_MEDIATOR;
	}

	public void put(Class<?> key, IRouteService value) {
		ROUTE_SERVICES.put(key, value);
	}

	public boolean contains(Class<?> key) {
		return ROUTE_SERVICES.containsKey(key);
	}

	public IRouteService get(Class<?> key) {
		return ROUTE_SERVICES.get(key);
	}
}
