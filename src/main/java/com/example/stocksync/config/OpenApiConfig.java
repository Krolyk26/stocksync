package com.example.stocksync.config;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI stockSyncOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Stock Sync Service API")
                        .description("REST API for syncing product stock from multiple vendors")
                        .version("v1.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation"));
    }
}
