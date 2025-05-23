package com.springboot.conferentieapp;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import perform.PerformRestConferentie;

@SpringBootApplication
@EnableJpaRepositories("repository")
@EntityScan("domein") 
@ComponentScan(basePackages = {"com.springboot.conferentieapp", "service"})
public class ConferentieApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(ConferentieApplication.class, args);
		try {
			new PerformRestConferentie();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/events");
		registry.addViewController("/403").setViewName("error/403");
		registry.addViewController("/404").setViewName("error/404");
		registry.addViewController("/500").setViewName("error/500");
	}
	
	@Bean
	LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.ENGLISH);
		return slr;
	}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }

}
