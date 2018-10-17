package com.coder520.mamabike;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.coder520.mamabike.common.swagger.FastJsonHttpMessageConverterEx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@PropertySource(value = "classpath:parameter.properties")
public class MamaBikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MamaBikeApplication.class, args);
	}

	/**
	 * FastJsonHttpMessageConverter
	 * @return
	 */
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverterEx();
		HttpMessageConverter<?> converter = fastConverter;
		return new HttpMessageConverters(converter);
	}


	/**
	 * 用于properties文件占位符解析
	 * @return
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
