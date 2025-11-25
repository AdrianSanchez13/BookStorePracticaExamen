package org.example.presentation.config;

import org.example.domain.repository.BookRepository;
import org.example.domain.service.BookService;
import org.example.domain.service.impl.BookServiceImpl;
import org.example.persistence.PersistenceConfig;
import org.example.persistence.dao.jpa.BookJpaDao;
import org.example.persistence.repository.BookRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PersistenceConfig.class)
public class SpringConfig {
    @Bean
    public BookRepository BookRepository(BookJpaDao bookJpaDao) {
        return new BookRepositoryImpl(bookJpaDao);
    }

    @Bean
    public BookService bookservice(BookRepository bookRepository) {
        return new BookServiceImpl(bookRepository);
    }
}
