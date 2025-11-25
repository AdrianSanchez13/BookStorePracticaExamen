package org.example.persistence.dao.jpa.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.persistence.TestConfig;
import org.example.persistence.dao.jpa.BookJpaDao;
import org.example.persistence.dao.jpa.entity.BookJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
class BookJpaDaoImplTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private BookJpaDao bookJpaDao;

    @Test
    @DisplayName("Test insert method persists BookJpaEntity")
    void testInsert() {


        BookJpaEntity newBook = new BookJpaEntity(
                null,
                "666666666666",
                "New Book Title ES",
                BigDecimal.valueOf(29.99),
                new BigDecimal(10.0)
        );

        String sql = "SELECT COUNT(b) FROM BookJpaEntity b";
        long countBefore = entityManager.createQuery(sql, Long.class)
                .getSingleResult();

        BookJpaEntity result = bookJpaDao.insert(newBook);

        long countAfter = entityManager.createQuery(sql, Long.class)
                .getSingleResult();

        long lastId = entityManager.createQuery("SELECT MAX(b.id) FROM BookJpaEntity b", Long.class)
                .getSingleResult();



        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(lastId, result.getId()),
                () -> assertEquals(newBook.getIsbn(), result.getIsbn()),
                () -> assertEquals(newBook.getTitleEs(), result.getTitleEs()),
                () -> assertEquals(newBook.getBasePrice(), result.getBasePrice()),
                () -> assertEquals(newBook.getDiscountPercentage(), result.getDiscountPercentage()),
                () -> assertEquals(countBefore + 1, countAfter)
        );
    }

    @Test
    void update_simple_fields_of_book_when_isbn_exists() {

        // Given - Primero insertamos el libro
        BookJpaEntity existingBook = new BookJpaEntity(
                null,
                "9780156055595",
                "El Principito",
                new BigDecimal("10.99"),
                new BigDecimal("0.00")
        );

        BookJpaEntity insertedBook = bookJpaDao.insert(existingBook);
        entityManager.flush(); // Asegura que se persiste antes de actualizar

        // When - Ahora actualizamos con el ID generado
        BookJpaEntity bookToUpdate = new BookJpaEntity(
                insertedBook.getId(), // Usamos el ID generado
                "9780156055595",
                "El Principito - Updated",
                new BigDecimal("99.99"),
                new BigDecimal("99.00")
        );

        BookJpaEntity updatedBook = bookJpaDao.update(bookToUpdate);

        // Then
        assertNotNull(updatedBook);
        assertEquals("El Principito - Updated", updatedBook.getTitleEs());
        assertEquals(new BigDecimal("99.99"), updatedBook.getBasePrice());
        assertEquals(new BigDecimal("99.00"), updatedBook.getDiscountPercentage());
    }

    @Test
    void delete_book_by_id() {
        // Given - Buscamos un libro existente
        var existingBook = bookJpaDao.findByIsbn("9780156012195");
        assertTrue(existingBook.isPresent());

        Long bookId = existingBook.get().getId();

        // When - Eliminamos el libro
        bookJpaDao.deleteById(bookId);
        entityManager.flush();

        // Then - Verificamos que ya no existe
        assertFalse(bookJpaDao.findById(bookId).isPresent());
    }

    @Test
    void findBook_By_Isbn_Native() {
        // Given - Primero insertamos el libro
        BookJpaEntity existingBook = new BookJpaEntity(
                null,
                "999999999999",
                "Native Query Book",
                new BigDecimal("15.99"),
                new BigDecimal("5.00")
        );

        BookJpaEntity insertedBook = bookJpaDao.insert(existingBook);
        entityManager.flush(); // Asegura que se persiste antes de buscar

        // When - Ahora buscamos por ISBN usando la consulta nativa
        var result = bookJpaDao.findByIsbn("999999999999");

        // Then
        assertTrue(result.isPresent());
        assertEquals(insertedBook.getId(), result.get().getId());
        assertEquals("Native Query Book", result.get().getTitleEs());
    }

    @Test
    void findAll_With_Pagination() {
        // Given - Ya existen 24 libros en la BD por Flyway
        // Verificamos que la BD esté cargada
        long totalBooks = bookJpaDao.count();
        assertEquals(24, totalBooks);

        // When - Buscamos la primera página con tamaño 10
        int page = 1;
        int size = 10;
        List<BookJpaEntity> result = bookJpaDao.findAll(page, size);

        // Then
        assertEquals(size, result.size());

        // Verificamos los primeros 3 ISBNs según V2__Seed_database.sql
        List<String> expectedIsbns = List.of(
                "9780156012195",  // El principito
                "9780140328721",  // Matilda
                "9780142410318"   // Charlie y la fábrica de chocolate
        );

        List<String> actualIsbns = result.stream()
                .limit(3)
                .map(BookJpaEntity::getIsbn)
                .collect(Collectors.toList());

        assertEquals(expectedIsbns, actualIsbns);

        // Verificamos títulos
        assertEquals("El principito", result.get(0).getTitleEs());
        assertEquals("Matilda", result.get(1).getTitleEs());
        assertEquals("Charlie y la fábrica de chocolate", result.get(2).getTitleEs());
    }

}