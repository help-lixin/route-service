package help.lixin.route.config;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.EurekaClient;
import help.lixin.route.eureka.CloudEurekaClientWrapper;
import help.lixin.route.eureka.IEurekaIInstanceInterceptor;
import help.lixin.route.eureka.EurekaIInstanceInterceptorFace;
import help.lixin.route.interceptor.IInstanceInterceptorFace;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.cloud.util.ProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Configuration
@ConditionalOnClass(EurekaClient.class)
public class EurekaClientConfig {

    @Bean
    public IInstanceInterceptorFace<InstanceInfo> instanceInterceptorFace(@Autowired(required = false) List<IEurekaIInstanceInterceptor> instanceInterceptors) {
        EurekaIInstanceInterceptorFace instanceInterceptorFace = new EurekaIInstanceInterceptorFace();
        if (null != instanceInterceptors) {
            instanceInterceptorFace.setInterceptors(instanceInterceptors);
        }
        return instanceInterceptorFace;
    }

    @Configuration
    protected static class RefreshableEurekaClientConfiguration {
        @Autowired
        private ApplicationContext context;

        @Autowired
        private AbstractDiscoveryClientOptionalArgs<?> optionalArgs;

        @Bean(destroyMethod = "shutdown")
        @ConditionalOnMissingBean(value = EurekaClient.class, search = SearchStrategy.CURRENT)
        @org.springframework.cloud.context.config.annotation.RefreshScope
        @Lazy
        public EurekaClient eurekaClient(ApplicationInfoManager manager,
                                         //
                                         com.netflix.discovery.EurekaClientConfig config,
                                         //
                                         EurekaInstanceConfig instance,
                                         //
                                         IInstanceInterceptorFace<InstanceInfo> instanceInterceptorFace,
                                         //
                                         @Autowired(required = false) HealthCheckHandler healthCheckHandler) {
            ApplicationInfoManager appManager;
            if (AopUtils.isAopProxy(manager)) {
                appManager = ProxyUtils.getTargetObject(manager);
            } else {
                appManager = manager;
            }
            CloudEurekaClient cloudEurekaClientWrapper = new CloudEurekaClientWrapper(appManager, config, this.optionalArgs, this.context, instanceInterceptorFace);
            cloudEurekaClientWrapper.registerHealthCheck(healthCheckHandler);
            return cloudEurekaClientWrapper;
        }
    }
}
