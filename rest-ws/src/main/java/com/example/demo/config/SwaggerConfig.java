package com.example.demo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
@EnableSwagger2
@ConfigurationProperties("app.api")
@ConditionalOnProperty(name="app.api.swagger.enable", havingValue = "true")
public class SwaggerConfig {

    private String version;
	private String title;
	private String description;
	private String basePackage;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
        	.select()
        	.apis(RequestHandlerSelectors.basePackage(basePackage))
        	.paths(PathSelectors.any())
        	.build()
        	.directModelSubstitute(LocalDate.class, java.sql.Date.class)
        	.directModelSubstitute(LocalDateTime.class, java.util.Date.class)
        	.apiInfo(apiInfo());
        }

        private ApiInfo apiInfo() {
        	return new ApiInfoBuilder()
        		.title(title)
        		.description(description)
        		.version(version)
        		.build();
        }

    public String getVersion() {
        return version;
    }

    public SwaggerConfig setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SwaggerConfig setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SwaggerConfig setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public SwaggerConfig setBasePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }

}
