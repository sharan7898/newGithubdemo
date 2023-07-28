package com.swayaan.nysf;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EntityScan({ "com.swayaan.nysf.entity" })
@EnableAsync
public class NysfKheloindiaYouthApplication {
	
//	@Bean
//	public FilterRegistrationBean<CustomURLRewriter> tuckeyRegistrationBean() {
//		final FilterRegistrationBean<CustomURLRewriter> registrationBean = new FilterRegistrationBean<CustomURLRewriter>();
//		registrationBean.setFilter(new CustomURLRewriter());
//		return registrationBean;
//	}

	public static void main(String[] args) {
		SpringApplication.run(NysfKheloindiaYouthApplication.class, args);
	}

	

}
