package com.lunar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by gadominas on 10/30/16.
 */
@Configuration
public class SwaggerConfigurer {
    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("crawler")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("PlatformLunar")
                .description("PlatformLunar Crawler System")
                .termsOfServiceUrl("https://www.linkedin.com/company/platform-lunar")
                .version("0.0.1-SNAPSHOT")
                .build();
    }
}
