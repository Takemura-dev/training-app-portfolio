package com.example.training.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        //JWTの認証の設定
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("bearerAuth");

        SecurityRequirement securityRequirement =
                new SecurityRequirement()
                        .addList("bearerAuth");

        return new OpenAPI()
                .info(new Info()
                        .title("Training App API")
                        .description("筋トレ記録アプリの API ドキュメント")
                        .version("1.0.0")
                )
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme)
                )
                .addSecurityItem(securityRequirement);
    }
}
