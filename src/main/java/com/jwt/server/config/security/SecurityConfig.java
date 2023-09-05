package com.jwt.server.config.security;

import com.jwt.server.dto.ExpirationTime;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true
)
public class SecurityConfig {

    @ConfigurationProperties(prefix = "expiration-time.access")
    @Bean("accessExpirationTime")
    public ExpirationTime accessExpirationTime(){
        return new ExpirationTime();
    }

    @ConfigurationProperties(prefix = "expiration-time.refresh")
    @Bean("refreshExpirationTime")
    public ExpirationTime refreshExpirationTime(){
        return new ExpirationTime();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new CustomPasswordEncoder();
    }
}
