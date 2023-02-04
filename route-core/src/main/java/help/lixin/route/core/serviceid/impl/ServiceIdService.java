package help.lixin.route.core.serviceid.impl;

import help.lixin.route.constants.Constants;
import help.lixin.route.core.serviceid.IServiceIdService;
import help.lixin.route.core.serviceid.model.BaseServiceId;
import help.lixin.route.core.serviceid.model.IServiceId;
import help.lixin.route.model.IRouteInfo;
import help.lixin.route.model.RouteInfo;

public class ServiceIdService implements IServiceIdService {

    @Override
    public String encode(String serviceId, IRouteInfo routeInfo) {
        if (null != routeInfo && routeInfo instanceof RouteInfo) {
            RouteInfo tmpRouteInfo = (RouteInfo) routeInfo;
            String group = null != tmpRouteInfo.getGroup() ? tmpRouteInfo.getGroup() : Constants.DEFAULT_GROUP;
            serviceId = String.format(Constants.SERVICE_ID_FORMAT, group, serviceId, tmpRouteInfo.getIp(), tmpRouteInfo.getPort());
        }
        return serviceId;
    }

    @Override
    public IServiceId decode(String serviceId) {
        IServiceId res = null;
        String[] serviceContextArray = serviceId.split("#");
        if (serviceContextArray.length == 4) {
            String group = serviceContextArray[0];
            String targetServiceId = serviceContextArray[1];
            String ip = serviceContextArray[2];
            Integer port = Integer.parseInt(serviceContextArray[3]);

            res = new BaseServiceId();
            res.setGroup(group);
            res.setServiceId(targetServiceId);
            res.setIp(ip);
            res.setPort(port);
        }
        return res;
    }
}
