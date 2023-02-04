package help.lixin.route.core.serviceid.model;

import java.util.Map;

public interface IServiceId {

    public String getGroup();

    public void setGroup(String group);

    public String getServiceId();

    public void setServiceId(String serviceId);

    public String getIp();

    public void setIp(String ip);

    public Integer getPort();

    public void setPort(Integer port);

    public Map<String, Object> getOthers();

    public void setOthers(Map<String, Object> others);
}
