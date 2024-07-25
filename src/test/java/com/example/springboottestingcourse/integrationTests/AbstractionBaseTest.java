package com.example.springboottestingcourse.integrationTests;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
/*
    this is a class used to configure testContainers and follows the singleton design pattern
    it can be used with multiple integration tests
 */
public abstract class AbstractionBaseTest {

    static final MySQLContainer mysql;


    static {
        mysql = new MySQLContainer<>("mysql:latest")
                .withUsername("user").withPassword("root").withDatabaseName("ems");
        mysql.start();
    }

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.username", mysql::getUsername);
    }

}
