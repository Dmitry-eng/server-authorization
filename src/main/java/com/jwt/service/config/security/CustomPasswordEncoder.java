package com.jwt.service.config.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CustomPasswordEncoder implements PasswordEncoder {
    @Value("${sha.key}")
    private String key;
    private static final String ALGORITHM = "SHA-256";

    @SneakyThrows
    public String encode(CharSequence rawPassword) {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.update(Decoders.BASE64.decode(key));
        byte[] bytes = md.digest(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
        return Encoders.BASE64.encode(bytes);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
