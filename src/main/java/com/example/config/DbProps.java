package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@ConfigurationProperties(prefix = "db")
public class DbProps {
    private String url;
    private String user;
    private String password;
}
