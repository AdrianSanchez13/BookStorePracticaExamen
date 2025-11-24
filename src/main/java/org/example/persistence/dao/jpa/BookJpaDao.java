package org.example.persistence.dao.jpa;

import org.example.domain.model.Page;
import org.example.persistence.dao.jpa.entity.BookJpaEntity;

import java.util.List;
import java.util.Optional;

public interface BookJpaDao {
    Optional<BookJpaEntity> findByIsbn(String isbn);
    void deleteByIsbn(String isbn);
    List<BookJpaEntity> findAll(int page, int size);
    Optional<BookJpaEntity> findById(Long id);
    BookJpaEntity insert(BookJpaEntity jpaEntity);
    BookJpaEntity update(BookJpaEntity jpaEntity);
    void deleteById(Long id);
    long count();

}
