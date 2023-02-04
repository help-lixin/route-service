package help.lixin.route.core.serviceid;

import help.lixin.route.core.serviceid.model.IServiceId;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;

public interface IServiceIdService {

    /**
     * 把serviceId进行编码成,一个新的serviceId
     *
     * @param serviceId
     * @return
     */
    String encode(String serviceId, IRouteInfo routeInfo);

    /**
     * 把serviceid转换成:IServiceId
     *
     * @param serviceId
     * @return
     */
    IServiceId decode(String serviceId);
}
