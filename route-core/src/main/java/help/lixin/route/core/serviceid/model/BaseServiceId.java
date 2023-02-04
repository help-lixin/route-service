package help.lixin.route.core.serviceid.model;

import java.util.HashMap;
import java.util.Map;

public class BaseServiceId implements IServiceId {
    private String group = "default";
    private String serviceId;
    private String ip;
    private Integer port = 80;
    private Map<String, Object> others = new HashMap<>();

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Map<String, Object> getOthers() {
        return others;
    }

    public void setOthers(Map<String, Object> others) {
        this.others = others;
    }
}
