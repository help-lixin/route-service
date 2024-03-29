package help.lixin.route.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RouteInfo implements IRouteInfo {

    private String group;

    // 微服务名称
    private String serviceId;
    // ip地址
    private String ip;
    // 端口
    private int port;
    // 其它信息
    private Map<String, Object> others = new HashMap<>();

    @Override
    public Map<String, Object> getOthers() {
        return others;
    }

    @Override
    public void setOthers(Map<String, Object> others) {
        if (null != others) {
            this.others = others;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Logger logger = LoggerFactory.getLogger(RouteInfo.class);
        // 组
        private String group;

        // 微服务名称
        private String serviceId;
        // ip地址
        private String ip;
        // 端口
        private Integer port;

        // 其它信息
        private Map<String, Object> others = new HashMap<String, Object>();

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public Builder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public Builder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public Builder others(Map<String, Object> others) {
            if (null != others) {
                this.others.putAll(others);
            }
            return this;
        }

        public Builder others(String key, Object value) {
            if (null != key && null != value) {
                this.others.put(key, value);
            }
            return this;
        }

        /**
         * ip:port
         *
         * @param address
         */
        public Builder address(String address) {
            String[] ipAndPort = address.split(":");
            if (ipAndPort.length == 2) {
                String ip = ipAndPort[0];
                int port = Integer.parseInt(ipAndPort[1]);
                this.ip = ip;
                this.port = port;
            } else {
                logger.warn("ignore address:[{}]", address);
            }
            return this;
        }

        public RouteInfo build() {
            RouteInfo routeInfo = new RouteInfo();
            routeInfo.setServiceId(serviceId);
            routeInfo.setIp(ip);
            routeInfo.setPort(port);
            routeInfo.setGroup(group);
            routeInfo.setOthers(others);
            return routeInfo;
        }
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

    public void setPort(int port) {
        this.port = port;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteInfo routeInfo = (RouteInfo) o;
        return serviceId.equals(routeInfo.serviceId);
    }

    @Override
    public String toString() {
        return "DefaultRouteInfo{" + "serviceId='" + serviceId + '\'' + ", ip='" + ip + '\'' + ", port=" + port + ", others=" + others + '}';
    }
}
