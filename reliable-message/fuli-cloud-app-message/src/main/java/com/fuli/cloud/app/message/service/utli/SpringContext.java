package com.fuli.cloud.app.message.service.utli;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public static <E> E getBean(final String beanName) {

        Preconditions.checkArgument(StringUtils.isNotEmpty(beanName));
        return (E) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(final Class<T> beanType) {
        Preconditions.checkNotNull(beanType);
        return applicationContext.getBean(beanType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
    }

}
