package com.springboot.conferentieapp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

@Configuration
public class ValidatorConfig {

    @Bean
    public LocalValidatorFactoryBean validator(ApplicationContext applicationContext, MessageSource messageSource) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();

        factoryBean.setConstraintValidatorFactory(
            new SpringConstraintValidatorFactory(applicationContext.getAutowireCapableBeanFactory())
        );

        factoryBean.setValidationMessageSource(messageSource);

        return factoryBean;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:i18n/messages"); 
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}
