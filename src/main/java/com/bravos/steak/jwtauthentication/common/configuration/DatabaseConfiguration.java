package com.bravos.steak.jwtauthentication.common.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl(getJdbcUrl());
        return dataSource;
    }

    private static String getJdbcUrl() {
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl == null || dbUrl.isEmpty()) {
            throw new IllegalArgumentException("Database URL is not set in the environment variables.");
        }
        if (dbUrl.startsWith("jdbc:")) {
            return dbUrl;
        }
        if (dbUrl.contains("@")) {
            return parseCustomDbUrl(dbUrl);
        }
        return "jdbc:" + dbUrl;
    }

    private static String parseCustomDbUrl(String dbUrl) {
        int atIndex = dbUrl.lastIndexOf('@');
        if (atIndex == -1) {
            throw new IllegalArgumentException("Invalid database URL format.");
        }
        String credentials = dbUrl.substring(0, atIndex);
        String hostPart = dbUrl.substring(atIndex + 1);
        int skip = credentials.indexOf("://") + 3;
        int colonIndex = credentials.indexOf(':',skip);
        if (colonIndex == -1) {
            throw new IllegalArgumentException("Invalid credentials format.");
        }
        String username = credentials.substring(skip, colonIndex);
        String password = credentials.substring(colonIndex + 1);

        String regex = "([^:]+):(\\d+)/(\\w+)";
        Matcher matcher = Pattern.compile(regex).matcher(hostPart);
        if (matcher.matches()) {
            String host = matcher.group(1);
            String port = matcher.group(2);
            String db = matcher.group(3);
            return String.format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s", host, port, db, username, password);
        }
        throw new IllegalArgumentException("Invalid host part format.");
    }

}
