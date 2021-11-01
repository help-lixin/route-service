package help.lixin.route.parse;

import help.lixin.route.model.RouteInfoList;

/**
 * 转换路由信息到:RouteInfo
 * 
 * @author infinova
 *
 */
public interface IRouteParseService {
	/**
	 * @param xroute 协议头里的信息.
	 * @return RouteInfoCollection 最终所有数据的载体
	 */
	RouteInfoList parse(String xroute);
}
