package com.finance.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FinanceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceBackendApplication.class, args);
    }

    // <-- Put the test bean here
    @Bean
    public CommandLineRunner testDbEnv(@Value("${spring.datasource.url}") String dbUrl) {
        return args -> System.out.println("DB URL Spring sees: " + dbUrl);
    }
}