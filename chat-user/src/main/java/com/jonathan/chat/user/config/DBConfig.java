package com.jonathan.chat.user.config;

import com.jonathan.chat.user.config.properties.AppProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DBConfig {

    private final AppProperties props;

    @Bean
    DataSource dataSource(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername(props.getDbUsername());
        hikariConfig.setPassword(props.getDbPassword());
        hikariConfig.setJdbcUrl(props.getDbUrl());
        hikariConfig.setConnectionTimeout(props.getDbConnTimeout());

        log.info("app.datasource.url={}", props.getDbUrl());
        log.info("app.datasource.connectionTimeout={}", props.getDbConnTimeout());

        return new HikariDataSource(hikariConfig);
    }
}
