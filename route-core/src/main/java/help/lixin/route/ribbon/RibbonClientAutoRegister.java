package help.lixin.route.ribbon;

import help.lixin.route.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClientSpecification;

import java.util.HashSet;
import java.util.Set;

public class RibbonClientAutoRegister implements BeanFactoryPostProcessor {
    private Logger logger = LoggerFactory.getLogger(RibbonClientAutoRegister.class);

    enum DiscoveryType {
        NACOS, EUREKA
    }

    private Set<String> excludeDiscoverys = new HashSet<>();

    {
        excludeDiscoverys.add("compositeDiscoveryClient");
        excludeDiscoverys.add("simpleDiscoveryClient");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            try {
                DiscoveryType type = parseDiscoveryType(beanFactory);
                DefaultListableBeanFactory registry = (DefaultListableBeanFactory) beanFactory;

                if (type.equals(DiscoveryType.EUREKA)) {
                    String name = "default." + Constants.EUREKA_RIBBON_CLIENT_CLASS;
                    Class<?> configuration = Class.forName(Constants.EUREKA_RIBBON_CLIENT_CLASS);
                    // 注册bean
                    registerBeanDefinition(registry, name, configuration);
                } else if (type.equals(DiscoveryType.NACOS)) {
                    String name = "default." + Constants.NACOS_RIBBON_CLIENT_CLASS;
                    Class<?> configuration = Class.forName(Constants.NACOS_RIBBON_CLIENT_CLASS);
                    // 注册bean
                    registerBeanDefinition(registry, name, configuration);
                }
            } catch (Exception e) {
                logger.warn("register bean fail:[{}]", e);
            }
        }
    }

    protected void registerBeanDefinition(DefaultListableBeanFactory registry, String name, Object configuration) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RibbonClientSpecification.class);
        builder.addConstructorArgValue(name);
        builder.addConstructorArgValue(configuration);
        registry.registerBeanDefinition(name + ".RibbonClientSpecification", builder.getBeanDefinition());
    }

    protected DiscoveryType parseDiscoveryType(ConfigurableListableBeanFactory beanFactory) {
        DiscoveryType type = null;
        String[] beanNames = beanFactory.getBeanNamesForType(DiscoveryClient.class);

        for (String beanName : beanNames) {
            if (excludeDiscoverys.contains(beanName)) {
                continue;
            }
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String factoryBeanName = beanDefinition.getFactoryBeanName();
            // 为什么要这样比较,是因为:怕有些开发人员搞自定义.
            if (factoryBeanName.contains("alibaba")) {
                type = DiscoveryType.NACOS;
            } else if (factoryBeanName.contains("springframework")) {
                type = DiscoveryType.EUREKA;
            }
        }
        return type;
    }
}