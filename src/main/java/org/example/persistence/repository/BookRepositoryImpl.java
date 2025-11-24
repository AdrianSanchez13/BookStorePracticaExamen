package org.example.persistence.repository;

import org.example.domain.model.Page;
import org.example.domain.repository.BookRepository;
import org.example.domain.repository.entity.BookEntity;
import org.example.persistence.dao.jpa.BookJpaDao;
import org.example.persistence.repository.mapper.BookMapperPersistence;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Optional;

public class BookRepositoryImpl implements BookRepository {


    private final BookJpaDao bookJpaDao;

    public BookRepositoryImpl(BookJpaDao bookJpaDao) {
        this.bookJpaDao = bookJpaDao;
    }

    @Override
    public void deleteByIsbn(String isbn) {
        bookJpaDao.deleteByIsbn(isbn);

    }

    @Override
    public Optional<BookEntity> findById(Long id) {
        return bookJpaDao.findById(id)
                .map(BookMapperPersistence.getInstance()::fromBookJpaEntityToBookEntity);
    }

    @Override
    public BookEntity save(BookEntity bookEntity) {
        return null;
    }

    @Override
    public Optional<BookEntity> findByIsbn(String isbn) {
        return bookJpaDao.findByIsbn(isbn)
                .map(BookMapperPersistence.getInstance()::fromBookJpaEntityToBookEntity);
    }

    @Override
    public Page<BookEntity> findAll(int page, int size) {
        return null;
    }
}
