package shop.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * freemarker-starter会自动配置freemarker，注册bean FreeMarkerConfigurer，
 * 我们需要进一步配置这个bean来注册freemarker的共享变量（可以在所有模板中使用的变量）
 */
@Component
public class FreeMarkerConfigurerBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof FreeMarkerConfigurer) {
            FreeMarkerConfigurer freeMarkerConfigurer = (FreeMarkerConfigurer) bean;
            Map<String, Object> variables = new HashMap<>();
            variables.put("sec", new SecurityHelper()); // 注册共享变量sec，用于在模板中获取当前用户的登录情况
            freeMarkerConfigurer.setFreemarkerVariables(variables);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

}
