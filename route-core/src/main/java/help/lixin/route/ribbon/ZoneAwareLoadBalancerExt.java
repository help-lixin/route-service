package help.lixin.route.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;

import java.util.Collections;
import java.util.List;

/**
 * ZoneAwareLoadBalancer内部有缓存:Server,影响路由重写.<br/>
 * 为什么是重写该类(ZoneAwareLoadBalancer),而不是:ServerListFilter,不期望路由重写功能的入口太多了.
 */
public class ZoneAwareLoadBalancerExt extends ZoneAwareLoadBalancer {


    public ZoneAwareLoadBalancerExt(IClientConfig clientConfig, IRule rule, IPing ping, ServerList serverList, ServerListFilter filter, ServerListUpdater serverListUpdater) {
        super(clientConfig, rule, ping, serverList, filter, serverListUpdater);
        // serverListUpdater是定时任务,取消掉.
        stopServerListRefreshing();
    }

    /**
     * 重写拿Server功能,直接调用EurekaClient去拿
     *
     * @return
     */
    public List<Server> getReachableServers() {
        ServerList serverListImpl = getServerListImpl();
        if (null != serverListImpl) {
            return serverListImpl.getUpdatedListOfServers();
        }
        return Collections.unmodifiableList(upServerList);
    }

    @Override
    public void updateListOfServers() {
        // 去除初始化Server列表.
    }

    /**
     * 重写拿Server功能,直接调用EurekaClient去拿
     *
     * @return
     */
    @Override
    public List<Server> getAllServers() {
        ServerList serverListImpl = getServerListImpl();
        if (null != serverListImpl) {
            return serverListImpl.getUpdatedListOfServers();
        }
        return Collections.unmodifiableList(allServerList);
    }

}
