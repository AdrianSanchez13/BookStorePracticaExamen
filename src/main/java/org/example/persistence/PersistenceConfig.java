package org.example.persistence;

import org.example.persistence.dao.jpa.BookJpaDao;
import org.example.persistence.dao.jpa.impl.BookJpaDaoImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.example.persistence.dao.jpa")
@EntityScan(basePackages = "org.example.persistence.dao.jpa.entity")
public class PersistenceConfig {
    @Bean
    public BookJpaDao bookJpaDao() {
        return new BookJpaDaoImpl();
    }
}
