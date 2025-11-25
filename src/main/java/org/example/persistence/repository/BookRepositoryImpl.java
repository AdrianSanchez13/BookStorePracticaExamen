package org.example.persistence.repository;

import org.example.domain.model.Page;
import org.example.domain.repository.BookRepository;
import org.example.domain.repository.entity.BookEntity;
import org.example.persistence.dao.jpa.BookJpaDao;
import org.example.persistence.repository.mapper.BookMapperPersistence;


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
        return BookMapperPersistence.getInstance()
                .fromBookJpaEntityToBookEntity(
                        bookJpaDao.insert(
                                BookMapperPersistence.getInstance()
                                        .fromBookEntityToBookJpaEntity(bookEntity)
                        )
                );
    }

    @Override
    public Optional<BookEntity> findByIsbn(String isbn) {
        return bookJpaDao.findByIsbn(isbn)
                .map(BookMapperPersistence.getInstance()::fromBookJpaEntityToBookEntity);
    }

    @Override
    public Page<BookEntity> findAll(int page, int size) {
        var bookJpaEntities = bookJpaDao.findAll(page, size);
        var bookEntities = bookJpaEntities.stream()
                .map(BookMapperPersistence.getInstance()::fromBookJpaEntityToBookEntity)
                .toList();
        var totalElements = bookJpaDao.count();
        return new Page<>(bookEntities, page, size, totalElements);
    }
}
