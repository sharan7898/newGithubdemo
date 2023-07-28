package com.swayaan.nysf.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {     
	private static final Logger logger= LoggerFactory.getLogger(SpringFoxConfig.class);
    @Bean
    public Docket api() { 
    	logger.info("Entered Config-> api  Method");
    	logger.info("Exit Config-> api  Method");
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build().apiInfo(metaData());                                           
    }
   
    private ApiInfo metaData() {
    	logger.info("Entered Config-> metaData  Method");
        ApiInfo apiInfo = new ApiInfo("NYSF Live Implementation", "APIs for KheloIndia", "v1", "", "", "", "");
        logger.info("Exit Config-> metaData  Method");
        return apiInfo;
    }
}