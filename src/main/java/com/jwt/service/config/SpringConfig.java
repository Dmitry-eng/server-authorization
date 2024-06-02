package com.jwt.service.config;

import com.jwt.service.dto.ExpirationTime;
import com.jwt.service.tool.JwtHelper;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.crypto.SecretKey;

@Configuration
public class SpringConfig {
    @Value("${jwt.secret.access}")
    private String jwtAccessSecret;
    @Value("${jwt.secret.refresh}")
    private String jwtRefreshSecret;

    @Bean
    public JwtHelper jwtTool(ExpirationTime accessExpirationTime, ExpirationTime refreshExpirationTime) {
        SecretKey accessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtAccessSecret));
        SecretKey refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtRefreshSecret));
        return new JwtHelper(accessSecretKey, refreshSecretKey, accessExpirationTime, refreshExpirationTime);
    }

    @Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(JstlView.class);
        bean.setPrefix("/templates/");
        bean.setSuffix(".html");
        return bean;
    }
}