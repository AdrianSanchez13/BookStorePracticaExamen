package org.example.persistence.repository.mapper;

import org.example.domain.repository.entity.BookEntity;
import org.example.persistence.dao.jpa.entity.BookJpaEntity;

public class BookMapperPersistence {
    private static BookMapperPersistence INSTANCE;

    private BookMapperPersistence() {
    }

    public static BookMapperPersistence getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookMapperPersistence();
        }
        return INSTANCE;
    }

    public BookJpaEntity fromBookEntityToBookJpaEntity(BookEntity bookEntity) {
        if (bookEntity == null) {
            return null;
        }

        return new BookJpaEntity(
                bookEntity.id(),
                bookEntity.isbn(),
                bookEntity.titleEs(),
                bookEntity.basePrice(),
                bookEntity.discountPercentage()
        );
    }

    public BookEntity fromBookJpaEntityToBookEntity(BookJpaEntity bookJpaEntity) {
        if (bookJpaEntity == null) {
            return null;
        }

        return new BookEntity(
                bookJpaEntity.getId(),
                bookJpaEntity.getIsbn(),
                bookJpaEntity.getTitleEs(),
                bookJpaEntity.getBasePrice(),
                bookJpaEntity.getDiscountPercentage()
        );
    }

}
