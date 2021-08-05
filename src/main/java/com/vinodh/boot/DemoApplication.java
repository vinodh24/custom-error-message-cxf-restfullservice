package com.vinodh.boot;

import java.util.Arrays;
import java.util.LinkedList;

import javax.ws.rs.Path;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.spring.SpringResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.vinodh.exceptionhandling.GenericExceptionMapper;

@SpringBootApplication
@EntityScan("com.vinodh.entity")	
@ComponentScan(basePackages = "com.*")
@EnableJpaRepositories(basePackages = "com.vinodh.repository", entityManagerFactoryRef="jpaEntityManagerFactoryBean")
@EnableTransactionManagement
public class DemoApplication {
	
	@Autowired
    private ApplicationContext ctx;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@Bean
    public Server jaxRsServer() {
        LinkedList<ResourceProvider> resourceProviders = new LinkedList<>();
        for (String beanName : ctx.getBeanDefinitionNames()) {
            if (ctx.findAnnotationOnBean(beanName, Path.class) != null) {
                SpringResourceFactory factory = new SpringResourceFactory(beanName);
                factory.setApplicationContext(ctx);
                resourceProviders.add(factory);
            }
        }

        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setBus(ctx.getBean(SpringBus.class));
        factory.setProviders(Arrays.asList(new JacksonJsonProvider(),new GenericExceptionMapper()));
//      factory.setProviders(Arrays.asList(new JacksonJsonProvider(),new CustomGlobalExceptionHandler(),new ThrowableMapper()));
        factory.setResourceProviders(resourceProviders);
        return factory.create();
    }
}
