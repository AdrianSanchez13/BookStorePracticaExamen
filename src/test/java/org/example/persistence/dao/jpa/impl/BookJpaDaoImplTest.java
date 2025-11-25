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
                "9780156012195",
                "El Principito",
                new BigDecimal("10.99"),
                new BigDecimal("0.00")
        );

        BookJpaEntity insertedBook = bookJpaDao.insert(existingBook);
        entityManager.flush(); // Asegura que se persiste antes de actualizar

        // When - Ahora actualizamos con el ID generado
        BookJpaEntity bookToUpdate = new BookJpaEntity(
                insertedBook.getId(), // Usamos el ID generado
                "9780156012195",
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
        // Given - Primero insertamos el libro
        BookJpaEntity existingBook = new BookJpaEntity(
                null,
                "9780156012195",
                "El Principito",
                new BigDecimal("10.99"),
                new BigDecimal("0.00")
        );

        BookJpaEntity insertedBook = bookJpaDao.insert(existingBook);
        entityManager.flush(); // Asegura que se persiste antes de eliminar

        // When - Ahora eliminamos con el ID generado
        bookJpaDao.deleteById(insertedBook.getId());
        entityManager.flush(); // Asegura que se aplica la eliminación

        // Then - Verificamos que ya no existe
        assertFalse(bookJpaDao.findById(insertedBook.getId()).isPresent());
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

}