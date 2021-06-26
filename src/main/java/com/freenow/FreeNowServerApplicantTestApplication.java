package com.freenow;

import com.freenow.util.LoggingInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@EnableSwagger2
@SpringBootApplication
public class FreeNowServerApplicantTestApplication implements WebMvcConfigurer
{

    public static void main(String[] args)
    {
        SpringApplication.run(FreeNowServerApplicantTestApplication.class, args);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new LoggingInterceptor()).addPathPatterns("/**");
    }


    @Bean
    public Docket docket()
    {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(getClass().getPackage().getName()))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(generateApiInfo())
            .securitySchemes(Arrays.asList(apiKey()));
    }


    private ApiInfo generateApiInfo()
    {
        return new ApiInfoBuilder().title("freenow Server Applicant Test Service")
            .description("This service is to check the technology knowledge of a server applicant for freenow.")
            .version("Version 1.0 - mw")
            .license("Apache 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
            .termsOfServiceUrl("urn:tos")
            .build();
    }


    private ApiKey apiKey()
    {
        return new ApiKey("JWT_LOGIN", "Authorization", "header");
    }
}
