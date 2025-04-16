package com.gest.art.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "M.KONE",
                        email = "mamadou.koneamed@gmail.com",
                        url = "https://universe-groupe.com"
                ),
                description = "OpenApi documentation pour unverse groupe",
                title = "Specification des endpoints des API unverse groupe pack stock et immo",
                version = "1.0",
                license = @License(
                        name = "Licence unverse groupe",
                        url = "https://universe-gp.com"
                ),
                termsOfService = "universe.e-service"
        ),
        servers = {
                @Server(
                        description = "DEV",
                        url = "http://localhost:8085"
                ),
                @Server(
                        description = "PROD",
                        url = "https://localhost:8083"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
