package com.bekzatsk.authservice.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "com.bekzatsk.authservice")
class OpenApiConfigurationProperties {
    var openApi = OpenAPI()

    inner class OpenAPI {
        var title: String? = null
        var description: String? = null
        var apiVersion: String? = null
        var contact: Contact = Contact()
        var security: Security = Security()

        inner class Contact {
            var email: String? = null
            var name: String? = null
            var url: String? = null
        }

        inner class Security {
            var name: String? = null
            var scheme: String? = null
            var bearerFormat: String? = null
        }
    }
}
