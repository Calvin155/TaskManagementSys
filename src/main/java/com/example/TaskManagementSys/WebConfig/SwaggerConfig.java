package com.example.TaskManagementSys.WebConfig;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Management System")
                        .version("1.0")
                        .description("API Docs with Swagger"))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Bearer Authentication")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                        //Global Accept Header
                        .addParameters("AcceptHeader",
                                new Parameter()
                                        .in("header")
                                        .name("Accept")
                                        .description("Response format (application/json or application/xml)")
                                        .required(false)
                                        .schema(new StringSchema()._default("application/json")))
                        //Global X-API-Version Header (this is a CUSTOM header)
                        .addParameters("VersionHeader",
                                new Parameter()
                                        .in("header")
                                        .name("X-API-Version")
                                        .description("API version (1 or 2)")
                                        .required(false)
                                        .schema(new StringSchema()._default("1"))));
    }
}
