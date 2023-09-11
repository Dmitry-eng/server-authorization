package com.jwt.server.config.security;

import com.jwt.server.config.CustomUserDetailsService;
import com.jwt.server.config.filter.CookieFilter;
import com.jwt.server.dto.ExpirationTime;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig {

    @ConfigurationProperties(prefix = "expiration-time.access")
    @Bean("accessExpirationTime")
    public ExpirationTime accessExpirationTime() {
        return new ExpirationTime();
    }

    @ConfigurationProperties(prefix = "expiration-time.refresh")
    @Bean("refreshExpirationTime")
    public ExpirationTime refreshExpirationTime() {
        return new ExpirationTime();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomUserDetailsService service, CookieFilter cookieFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(service)
                .addFilterBefore(cookieFilter, AuthorizationFilter.class)
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/js/**").permitAll().requestMatchers("/login/**", "/account/auth/login").hasRole("ANONYMOUS").anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .permitAll()
                );
        return http.build();
    }
}


