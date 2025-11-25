package org.example.persistence.repository;

import org.example.domain.model.Page;
import org.example.domain.repository.BookRepository;
import org.example.domain.repository.entity.BookEntity;
import org.example.persistence.dao.jpa.BookJpaDao;
import org.example.persistence.dao.jpa.entity.BookJpaEntity;
import org.example.persistence.repository.mapper.BookMapperPersistence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRepositoryImplTest {

    @Mock
    private BookJpaDao bookJpaDao;

    @InjectMocks
    private BookRepositoryImpl bookRepository;

//    public class BookRepositoryImpl implements BookRepository {
//
//
//        private final BookJpaDao bookJpaDao;
//
//        public BookRepositoryImpl(BookJpaDao bookJpaDao) {
//            this.bookJpaDao = bookJpaDao;
//        }
//
//        @Override
//        public void deleteByIsbn(String isbn) {
//            bookJpaDao.deleteByIsbn(isbn);
//
//        }
//
//        @Override
//        public Optional<BookEntity> findById(Long id) {
//            return bookJpaDao.findById(id)
//                    .map(BookMapperPersistence.getInstance()::fromBookJpaEntityToBookEntity);
//        }
//
//        @Override
//        public BookEntity save(BookEntity bookEntity) {
//            var bookJpaEntity = BookMapperPersistence.getInstance().fromBookEntityToBookJpaEntity(bookEntity);
//            BookJpaEntity savedBookJpaEntity;
//            if (bookJpaEntity.getId() == null) {
//                savedBookJpaEntity = bookJpaDao.insert(bookJpaEntity);
//            } else {
//                savedBookJpaEntity = bookJpaDao.update(bookJpaEntity);
//            }
//            return BookMapperPersistence.getInstance().fromBookJpaEntityToBookEntity(savedBookJpaEntity);
//        }
//
//        @Override
//        public Optional<BookEntity> findByIsbn(String isbn) {
//            return bookJpaDao.findByIsbn(isbn)
//                    .map(BookMapperPersistence.getInstance()::fromBookJpaEntityToBookEntity);
//        }
//
//        @Override
//        public Page<BookEntity> findAll(int page, int size) {
//            var bookJpaEntities = bookJpaDao.findAll(page, size);
//            var bookEntities = bookJpaEntities.stream()
//                    .map(BookMapperPersistence.getInstance()::fromBookJpaEntityToBookEntity)
//                    .toList();
//            var totalElements = bookJpaDao.count();
//            return new Page<>(bookEntities, page, size, totalElements);
//        }
//    }

    @Test
    void deleteByIsbn() {
        BookRepository bookRepository = new BookRepositoryImpl(bookJpaDao);
        bookRepository.deleteByIsbn("1234567890");

        when(bookJpaDao.findByIsbn("1234567890")).thenReturn(Optional.empty());

        Optional<BookEntity> result = bookRepository.findByIsbn("1234567890");
        assertFalse(result.isPresent());
    }

    @Test
    void return_optional_bookEntity_when_isbn_exists() {
        BookJpaEntity bookJpaEntity = new BookJpaEntity(
                1L,
                "1234567890",
                "Test Book",
                new BigDecimal(12.99),
                new BigDecimal(10.00)
        );

        when(bookJpaDao.findByIsbn("1234567890"))
                .thenReturn(Optional.of(bookJpaEntity));

        Optional<BookEntity> result = bookRepository.findByIsbn("1234567890");

        assertTrue(result.isPresent());
        assertEquals("1234567890", result.get().isbn());

    }

    @Test
    void return_optional_empty_when_isbn_does_not_exist() {
        when(bookJpaDao.findByIsbn("non-existent-isbn"))
                .thenReturn(Optional.empty());

        Optional<BookEntity> result = bookRepository.findByIsbn("non-existent-isbn");

        assertFalse(result.isPresent());
    }

    @Test
    void save() {
    }


    @Test
    void return_page_of_bookEntity() {
        // Given
        BookJpaEntity bookJpaEntity = new BookJpaEntity(
                1L,
                "1234567890",
                "Test Book",
                new BigDecimal("12.99"),
                new BigDecimal("10.00")
        );

        when(bookJpaDao.findAll(1, 1))
                .thenReturn(java.util.List.of(bookJpaEntity));
        when(bookJpaDao.count()).thenReturn(1L);

        // When
        Page<BookEntity> result = bookRepository.findAll(1, 1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.data().size());

        // Verifica los datos del libro
        BookEntity returnedBook = result.data().get(0);
        assertEquals(1L, returnedBook.id());
        assertEquals("1234567890", returnedBook.isbn());
        assertEquals("Test Book", returnedBook.titleEs());
        assertEquals(new BigDecimal("12.99"), returnedBook.basePrice());
        assertEquals(new BigDecimal("10.00"), returnedBook.discountPercentage());

        // Verifica los metadatos de paginación
        assertEquals(1, result.pageNumber());
        assertEquals(1, result.pageSize());
        assertEquals(1L, result.totalElements());
    }


}