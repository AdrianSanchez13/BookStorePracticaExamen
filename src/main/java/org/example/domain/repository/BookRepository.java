package org.example.domain.repository;

import org.example.domain.model.Page;
import org.example.domain.repository.entity.BookEntity;

import java.util.Optional;

public interface BookRepository {

    Page<BookEntity> findAll(int page, int size);
    Optional<BookEntity> findByIsbn(String isbn);
    BookEntity save(BookEntity bookEntity);
    Optional<BookEntity> findById(Long id);
    void deleteByIsbn(String isbn);
}
