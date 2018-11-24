package com.juniormiqueletti.moneyapp;

import com.juniormiqueletti.moneyapp.config.property.MoneyApiProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(MoneyApiProperty.class)
public class MoneyApiApplication {

    private static ApplicationContext CONTEXT;

	public static void main(String[] args) {
        CONTEXT = SpringApplication.run(MoneyApiApplication.class, args);
	}

	public static <T> T getBean(Class<T> type) {
	    return CONTEXT.getBean(type);
    }

}
