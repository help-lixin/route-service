package help.lixin.route.model;

import java.util.Map;

/**
 * 路由信息定义
 */
public interface IRouteInfo {
    public String getServiceId();

    public void setServiceId(String serviceId);

    public Map<String, Object> getOthers();

    public void setOthers(Map<String, Object> others);
}
