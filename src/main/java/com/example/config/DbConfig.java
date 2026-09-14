package com.example.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableConfigurationProperties(DbProps.class)
@PropertySource("classpath:db.properties")
public class DbConfig {

    @Bean
    public DataSource dataSource(DbProps props) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getUrl());
        config.setUsername(props.getUser());
        config.setPassword(props.getPassword());
        return new HikariDataSource(config);
    }
}
