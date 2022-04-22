package com.jonathan.chat.gw.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Getter
public class AppProperties {

    @Value("${auth.secret}")
    private String secret;

    @Value("${auth.role.user}")
    private String roleUser;

    @Value("${auth.issuer}")
    private String issuer;
}
