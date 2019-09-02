package com.uad2.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class WebConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.uad2.application"))
                .paths(PathSelectors.ant("/**/api/**"))
                .build()
                .apiInfo(apiInfo());
        //.useDefaultResponseMessages(false)
        //.globalResponseMessage(RequestMethod.GET, getArrayList());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("UAD2 Rest Api")
                .description("UAD2 Rest Api 명세서 (URI 경로에 \"api\"가 포함된 Api만 포함)")
                .version("1.0.0")
                .build();
    }
}
