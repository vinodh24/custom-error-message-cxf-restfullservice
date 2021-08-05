package com.vinodh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath*:application-context.xml","classpath*:nocvue-database-app-context.xml","classpath*:rest-config.xml"})
public class BeansLoaderConfig {
	
}
