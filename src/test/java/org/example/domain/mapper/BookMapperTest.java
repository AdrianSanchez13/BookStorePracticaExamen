package org.example.domain.mapper;

import org.example.domain.model.Book;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {

    @Test
    void bookToBookDto() {
        Book book = new Book(
                1L,
                "978-3-16-148410-0",
                "Título en Español",
                new BigDecimal(23.50),
                new BigDecimal(10.00)
        );

        var bookDto = BookMapper.getInstance().fromBookToBookDto(book);
        assertNotNull(bookDto);
        assertEquals(book.getId(), bookDto.id());
        assertEquals(book.getIsbn(), bookDto.isbn());
        assertEquals(book.getTitleEs(), bookDto.titleEs());
        assertEquals(book.getBasePrice(), bookDto.basePrice());
        assertEquals(book.getDiscountPercentage(), bookDto.discountPercentage());
        assertEquals(book.calculateFinalPrice(), bookDto.price());



    }

    @Test
    void bookEntityToBook() {
        var bookEntity = new org.example.domain.repository.entity.BookEntity(
                1L,
                "978-3-16-148410-0",
                "Título en Español",
                new BigDecimal(23.50),
                new BigDecimal(10.00)
        );

        var book = BookMapper.getInstance().fromBookEntityToBook(bookEntity);
        assertNotNull(book);
        assertEquals(bookEntity.id(), book.getId());
        assertEquals(bookEntity.isbn(), book.getIsbn());
        assertEquals(bookEntity.titleEs(), book.getTitleEs());
        assertEquals(bookEntity.basePrice(), book.getBasePrice());
        assertEquals(bookEntity.discountPercentage(), book.getDiscountPercentage());
    }

    @Test
    void bookToBookEntity() {
        Book book = new Book(
                1L,
                "978-3-16-148410-0",
                "Título en Español",
                new BigDecimal(23.50),
                new BigDecimal(10.00)
        );

        var bookEntity = BookMapper.getInstance().fromBookToBookEntity(book);
        assertNotNull(bookEntity);
        assertEquals(book.getId(), bookEntity.id());
        assertEquals(book.getIsbn(), bookEntity.isbn());
        assertEquals(book.getTitleEs(), bookEntity.titleEs());
        assertEquals(book.getBasePrice(), bookEntity.basePrice());
        assertEquals(book.getDiscountPercentage(), bookEntity.discountPercentage());
    }

    @Test
    void bookDtoToBook() {
        var bookDto = new org.example.domain.service.dto.BookDto(
                1L,
                "978-3-16-148410-0",
                "Título en Español",
                new BigDecimal(23.50),
                new BigDecimal(10.00),
                new BigDecimal(21.15)
        );
        var book = BookMapper.getInstance().fromBookDtoToBook(bookDto);
        assertNotNull(book);
        assertEquals(bookDto.id(), book.getId());
        assertEquals(bookDto.isbn(), book.getIsbn());
        assertEquals(bookDto.titleEs(), book.getTitleEs());
        assertEquals(bookDto.basePrice(), book.getBasePrice());
        assertEquals(bookDto.discountPercentage(), book.getDiscountPercentage());
    }
}