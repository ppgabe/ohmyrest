package dev.ailuruslabs.ohmyrest;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OhmyrestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OhmyrestApplication.class, args);
    }

    @Bean
    public OpenAPI openAPI() {
        // 1. Define the security scheme (Bearer Auth)
        final String securitySchemeName = "bearerAuth";

        SecurityScheme bearerAuthScheme = new SecurityScheme()
            .name(securitySchemeName)
            .type(SecurityScheme.Type.HTTP) // We are using HTTP
            .scheme("bearer")               // The scheme is "bearer"
            .bearerFormat("JWT");           // The format is "JWT"

        // 2. Define a security requirement (what needs the auth)
        SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList(securitySchemeName); // Must match the name above

        // 3. Build the OpenAPI object
        return new OpenAPI()
            .addSecurityItem(securityRequirement) // Add the requirement globally
            .components(new Components()
                            .addSecuritySchemes(securitySchemeName, bearerAuthScheme) // Add the scheme definition
            );
    }

}
