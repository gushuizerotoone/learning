package com.gulewang.app.web;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * swagger-ui is accessible at
 * http://localhost:8080/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${spring.application.version}")
  private String serviceVersion;

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(createApiInfo())
            .select()
            .apis(Predicates.and(
                    Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")),
                    Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.cloud")))
            )
            .paths(PathSelectors.any())
            .build();
  }

  private ApiInfo createApiInfo() {
    return new ApiInfoBuilder()
            .title(applicationName)
            .description("")
            .termsOfServiceUrl("")
            .license("")
            .licenseUrl("")
            .version(serviceVersion)
            .build();
  }
}
