package com.gmail.burinigor7.usersservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
@Profile("test")
public class PostgreSQLContainerConfig {
    @Value("${testcontainer.postgresql.image}")
    private String imageName;

    @Value("${testcontainer.postgresql.database}")
    private String databaseName;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public PostgreSQLContainer postgreSQLContainer() {
        return new PostgreSQLContainer(imageName)
                .withDatabaseName(databaseName)
                .withUsername(username)
                .withPassword(password);
    }
}
