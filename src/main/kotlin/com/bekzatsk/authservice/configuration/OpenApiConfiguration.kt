package com.bekzatsk.authservice.configuration

import com.bekzatsk.authservice.configuration.properties.OpenApiConfigurationProperties
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(OpenApiConfigurationProperties::class)
@AllArgsConstructor
class OpenApiConfiguration {
    @Autowired
    private lateinit var openApiConfigurationProperties: OpenApiConfigurationProperties

    @Bean
    fun customOpenAPI(): OpenAPI? {
        val properties = openApiConfigurationProperties.openApi
        val security = properties.security
        val contact = properties.contact
        val info = Info().title(properties.title).version(properties.apiVersion)
            .description(properties.description)
            .contact(Contact().email(contact.email).name(contact.name).url(contact.url))
        return OpenAPI().info(info).addSecurityItem(SecurityRequirement().addList(security.name))
            .components(
                Components().addSecuritySchemes(
                    security.name,
                    SecurityScheme().name(security.name).type(SecurityScheme.Type.HTTP)
                        .scheme(security.scheme).bearerFormat(security.bearerFormat)
                )
            )
    }
}
