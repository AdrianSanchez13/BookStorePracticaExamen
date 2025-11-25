package org.example.domain.service.impl;

import org.example.domain.model.Page;
import org.example.domain.repository.BookRepository;
import org.example.domain.repository.entity.BookEntity;
import org.example.domain.service.dto.BookDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {


    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;


    @Test
    void return_page_of_BookDto_when_getAll_is_called_with_valid_arguments() {
        // Given
        BookEntity bookEntity1 = new BookEntity(
                1L,
                "9783161484100",
                "Test Book 1",
                new BigDecimal("100.00"),
                new BigDecimal("12.99")
        );
        BookEntity bookEntity2 = new BookEntity(
                2L,
                "9780987654321",
                "Test Book 2",
                new BigDecimal("150.00"),
                new BigDecimal("15.99")
        );

        List<BookEntity> bookEntities = List.of(bookEntity1, bookEntity2);
        Page<BookEntity> entityPage = new Page<>(bookEntities, 1, 2, 2);

        when(bookRepository.findAll(1, 2))
                .thenReturn(entityPage);


        Page<BookDto> result = bookService.getAll(1, 2);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.data()).hasSize(2);
        assertThat(result.pageNumber()).isEqualTo(1);
        assertThat(result.pageSize()).isEqualTo(2);
        assertThat(result.totalElements()).isEqualTo(2);

        BookDto firstBook = result.data().get(0);
        assertThat(firstBook.id()).isEqualTo(1L);
        assertThat(firstBook.isbn()).isEqualTo("9783161484100");
        assertThat(firstBook.titleEs()).isEqualTo("Test Book 1");
        assertThat(firstBook.basePrice()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    void return_empty_page_when_getAll_finds_no_books() {
        when(bookRepository.findAll(anyInt(), anyInt())).thenReturn(new Page<>(List.of(), 1, 10, 0));

        Page<BookDto> result = bookService.getAll(1, 10);

        assertThat(result.data()).isEmpty();
        assertThat(result.totalElements()).isZero();
        assertThat(result.totalPages()).isZero();
    }

    @Test
    void return_BookDto_when_getByIsbn_finds_a_book() {
        BookEntity bookEntity = new BookEntity(
                1L,
                "9783161484100",
                "Test Book",
                new BigDecimal("100.00"),
                new BigDecimal("12.99")
        );
        when(bookRepository.findByIsbn(anyString()))
                .thenReturn(Optional.of(bookEntity));

        BookDto result = bookService.getByIsbn(bookEntity.isbn());

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.isbn()).isEqualTo("9783161484100");
        assertThat(result.titleEs()).isEqualTo("Test Book");
        assertThat(result.basePrice()).isEqualByComparingTo(new BigDecimal("100.00"));

    }

    @Test
    void return_empty_optional_when_findByIsbn_finds_no_book() {
        String isbn = "non-existing-isbn";
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        Optional<BookDto> result = bookService.findByIsbn(isbn);

        assertThat(result).isNotPresent();
    }

    @Test
    void save_book_and_return_created_BookDto_when_create_book_with_valid_data() {
        BookDto bookDtoToCreate = new BookDto(
                2L,
                "9783161484100",
                "New Book",
                new BigDecimal("120.00"),
                new BigDecimal("10.00"),
                new BigDecimal("108.00")
        );

        BookEntity savedBookEntity = new BookEntity(
                1L,
                "9783161484100",
                "New Book",
                new BigDecimal("120.00"),
                new BigDecimal("10.00")
        );

        when(bookRepository.save(any(BookEntity.class))).thenReturn(savedBookEntity);

        BookDto result = bookService.create(bookDtoToCreate);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.isbn()).isEqualTo("9783161484100");
        assertThat(result.titleEs()).isEqualTo("New Book");
        assertThat(result.basePrice()).isEqualByComparingTo(new BigDecimal("120.00"));
    }

    @Test
    void update_book() {
        BookDto bookDtoToUpdate = new BookDto(
                1L,
                "9783161484100",
                "Updated Book",
                new BigDecimal("130.00"),
                new BigDecimal("5.00"),
                new BigDecimal("123.50")
        );

        BookEntity existingBookEntity = new BookEntity(
                1L,
                "9783161484100",
                "Old Book",
                new BigDecimal("120.00"),
                new BigDecimal("10.00")
        );

        BookEntity updatedBookEntity = new BookEntity(
                1L,
                "9783161484100",
                "Updated Book",
                new BigDecimal("130.00"),
                new BigDecimal("5.00")
        );

        when(bookRepository.findByIsbn(bookDtoToUpdate.isbn())).thenReturn(Optional.of(existingBookEntity));
        when(bookRepository.save(any(BookEntity.class))).thenReturn(updatedBookEntity);

        BookDto result = bookService.update(bookDtoToUpdate);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.isbn()).isEqualTo("9783161484100");
        assertThat(result.titleEs()).isEqualTo("Updated Book");
        assertThat(result.basePrice()).isEqualByComparingTo(new BigDecimal("130.00"));
    }

    @Test
    void throw_exception_when_update_book_with_non_existing_isbn() {
        BookDto bookDtoToUpdate = new BookDto(
                1L,
                "non-existing-isbn",
                "Updated Book",
                new BigDecimal("130.00"),
                new BigDecimal("5.00"),
                new BigDecimal("123.50")
        );

        when(bookRepository.findByIsbn(bookDtoToUpdate.isbn())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookService.update(bookDtoToUpdate));
    }

    @Test
    void delete_a_book_with_valid_isbn() {
        // Given
        String isbn = "9783161484100";
        BookEntity existingBook = new BookEntity(
                1L,
                isbn,
                "Test Book",
                new BigDecimal("100.00"),
                new BigDecimal("10.00")
        );

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(existingBook));

        // When
        bookService.deleteByIsbn(isbn);

        // Then
        verify(bookRepository).deleteByIsbn(isbn);
    }

    @Test
    void throw_exception_when_delete_book_with_non_existing_isbn() {
        // Given
        String nonExistingIsbn = "9999999999999";

        when(bookRepository.findByIsbn(nonExistingIsbn)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> bookService.deleteByIsbn(nonExistingIsbn));
    }
}