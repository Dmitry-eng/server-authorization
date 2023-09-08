package com.jwt.server.config.security;

//import com.jwt.server.config.CustomUserDetailsService;
import com.jwt.server.dto.ExpirationTime;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                        .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/test/**", "/account/auth/login").permitAll()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .defaultSuccessUrl("/welcome")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }
}


