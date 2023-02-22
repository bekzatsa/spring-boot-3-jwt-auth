package com.bekzatsk.authservice.security.constant

import lombok.AllArgsConstructor
import lombok.Getter

@AllArgsConstructor
class ApiPathExclusion {

    @AllArgsConstructor
    enum class GetApiPathExclusion {
        HEALTH_CHECK {
            override fun getPath() = "/ping"
        },
        SWAGGER_API_V2_DOCS {
            override fun getPath() = "/v2/api-docs"
        },
        SWAGGER_RESOURCE_CONFIGURATION {
            override fun getPath() = "/swagger-resources/configuration/ui"
        },
        SWAGGER_RESOURCES {
            override fun getPath() = "/swagger-resources"
        },
        SWAGGER_RESOURCES_SECURITY_CONFIGURATION {
            override fun getPath() = "/swagger-resources/configuration/security"
        },
        SWAGGER_UI_HTML {
            override fun getPath() = "swagger-ui.html"
        },
        WEBJARS {
            override fun getPath() = "/webjars/**"
        },
        SWAGGER_UI {
            override fun getPath() = "/swagger-ui/**"
        },
        SWAGGER_API_V3_DOCS {
            override fun getPath() = "/v3/api-docs/**"
        },
        SWAGGER_CONFIGURATION {
            override fun getPath() = "/configuration/**"
        },
        SWAGGER {
            override fun getPath() = "/swagger*/**"
        };

        abstract fun getPath(): String
    }

    @AllArgsConstructor
    enum class PostApiPathExclusion {
        SIGN_UP {
            override fun getPath() = "/users"
        },
        LOGIN {
            override fun getPath() = "/auth/login"
        };

        abstract fun getPath(): String
    }

    @AllArgsConstructor
    enum class PutApiPathExclusion {
        REFRESH_TOKEN {
            override fun getPath() = "/auth/refresh"
        };

        abstract fun getPath(): String
    }
}
