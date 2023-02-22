package com.bekzatsk.authservice.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "com.bekzatsk.authservice")
class JwtConfigurationProperties {
    var jwt = JWT()

    inner class JWT {
        var secretKey: String? = null
        var accessToken = AccessToken()
        var refreshToken = RefreshToken()

        inner class AccessToken {
            /**
             * validity of access-token in minutes
             */
            var validity: Long = 0
        }

        inner class RefreshToken {
            /**
             * validity of refresh-token in days
             */
            var validity: Long = 0
        }
    }
}
