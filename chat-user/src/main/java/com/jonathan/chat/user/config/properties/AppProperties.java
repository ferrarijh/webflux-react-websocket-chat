package com.jonathan.chat.user.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Getter
public class AppProperties {

    @Value("${app.secret}")
    private String secret;

    @Value("${app.duration.access-token}")
    private Integer accessTokenDuration;

    @Value("${app.duration.refresh-token}")
    private Integer refreshTokenDuration;

    @Value("${role.user}")
    private String roleUser;

    @Value("${app.issuer}")
    private String issuer;

    //============= hikari config ================
    @Value("${app.datasource.username}")
    private String dbUsername;

    @Value("${app.datasource.password}")
    private String dbPassword;

    @Value("${app.datasource.url}")
    private String dbUrl;

    @Value("${app.datasource.connectionTimeout}")
    private Long dbConnTimeout;

}