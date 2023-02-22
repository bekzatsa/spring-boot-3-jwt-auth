package com.bekzatsk.authservice.configuration

import com.bekzatsk.authservice.security.constant.ApiPathExclusion
import com.bekzatsk.authservice.security.filter.JwtAuthenticationFilter
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfiguration {
    @Autowired
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @Bean
    @Throws(Exception::class)
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http.cors().and().csrf().disable().exceptionHandling().and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                    HttpMethod.GET,
                    *ApiPathExclusion.GetApiPathExclusion.values()
                        .map { api -> api.getPath() }.toTypedArray())
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.POST,
                        *ApiPathExclusion.PostApiPathExclusion.values()
                            .map { api -> api.getPath() }.toTypedArray())
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.PUT,
                        *ApiPathExclusion.PutApiPathExclusion.values()
                            .map { api -> api.getPath() }.toTypedArray())
                    .permitAll().anyRequest().authenticated()
            }.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }
}
