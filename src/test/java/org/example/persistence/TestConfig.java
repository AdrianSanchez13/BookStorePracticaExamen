package org.example.persistence;

import org.example.persistence.dao.jpa.BookJpaDao;
import org.example.persistence.dao.jpa.impl.BookJpaDaoImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@TestConfiguration
@EnableJpaRepositories(basePackages = "org.example.persistence.dao.jpa")
@EntityScan(basePackages = "org.example.persistence.dao.jpa.entity")
@EnableAutoConfiguration
public class TestConfig {

    @Bean
    public BookJpaDao bookJpaDao() {
        return new BookJpaDaoImpl();
    }
}
