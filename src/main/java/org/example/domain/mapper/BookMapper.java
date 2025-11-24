package org.example.domain.mapper;

import org.example.domain.model.Book;
import org.example.domain.repository.entity.BookEntity;
import org.example.domain.service.dto.BookDto;

public class BookMapper {
    private static BookMapper INSTANCE;

    private BookMapper() {
    }

    public static BookMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookMapper();
        }
        return INSTANCE;
    }

    public Book fromBookEntityToBook(BookEntity bookEntity) {
        if (bookEntity == null) {
            return null;
        }

        return new Book(
                bookEntity.id(),
                bookEntity.isbn(),
                bookEntity.titleEs(),
                bookEntity.basePrice(),
                bookEntity.discountPercentage()
        );
    }

    public BookEntity fromBookToBookEntity(Book book) {
        if (book == null) {
            return null;
        }

        return new BookEntity(
                book.getId(),
                book.getIsbn(),
                book.getTitleEs(),
                book.getBasePrice(),
                book.getDiscountPercentage()
        );
    }

    public BookDto fromBookToBookDto(Book book) {
        if (book == null) {
            return null;
        }

        return new BookDto(
                book.getId(),
                book.getIsbn(),
                book.getTitleEs(),
                book.getBasePrice(),
                book.getDiscountPercentage(),
                book.calculateFinalPrice()
        );
    }

    public Book fromBookDtoToBook(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }

        return new Book(
                bookDto.id(),
                bookDto.isbn(),
                bookDto.titleEs(),
                bookDto.basePrice(),
                bookDto.discountPercentage()
        );
    }
}
