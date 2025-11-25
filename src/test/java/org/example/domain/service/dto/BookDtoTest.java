package org.example.domain.service.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.*;
import org.example.domain.exception.ValidationException;
import org.example.domain.model.Book;
import org.example.domain.validation.DtoValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BookDtoTest {


//    public record BookDto(
//            @NotNull
//            Long id,
//            @NotNull
//            @Size(min = 13, max = 13, message = "El ISBN debe tener exactamente 13 caracteres")
//            String isbn,
//            @NotBlank(message = "El título en español no puede estar vacío")
//            String titleEs,
//            @NotNull(message = "El precio base no puede ser nulo")
//            @DecimalMin(value = "0.0", inclusive = true, message = "El precio base debe ser mayor o igual a 0")
//            BigDecimal basePrice,
//            @NotNull
//            @DecimalMin(value = "0.0", inclusive = true, message = "El descuento no puede ser menor a 0")
//            @DecimalMax(value = "100.0", inclusive = true, message = "El descuento no puede ser mayor a 100")
//            BigDecimal discountPercentage,
//            BigDecimal price
//    ) {
//
//    }

    @Test
    void testBookDtoCreation() {
        Long id = 1L;
        String isbn = "9783161484100";
        String titleEs = "Título en Español";
        BigDecimal basePrice = new BigDecimal("100.00");
        BigDecimal discountPercentage = new BigDecimal("10.00");
        BigDecimal price = new BigDecimal("90.00");

        BookDto bookDto = new BookDto(id, isbn, titleEs, basePrice, discountPercentage, price);

        assertEquals(id, bookDto.id());
        assertEquals(isbn, bookDto.isbn());
        assertEquals(titleEs, bookDto.titleEs());
        assertEquals(basePrice, bookDto.basePrice());
        assertEquals(discountPercentage, bookDto.discountPercentage());
        assertEquals(price, bookDto.price());
    }

    @Test
    void testBookDtoValidation() {
        Long id = 1L;
        String isbn = "9783161484100";
        String titleEs = "Título en Español";
        BigDecimal basePrice = new BigDecimal("100.00");
        BigDecimal discountPercentage = new BigDecimal("10.00");
        BigDecimal price = new BigDecimal("90.00");

        BookDto bookDto = new BookDto(id, isbn, titleEs, basePrice, discountPercentage, price);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<BookDto>> violations = validator.validate(bookDto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    void testBookDtoInvalidIsbn() {
        Long id = 1L;
        String invalidIsbn = "123"; // ISBN inválido
        String titleEs = "Título en Español";
        BigDecimal basePrice = new BigDecimal("100.00");
        BigDecimal discountPercentage = new BigDecimal("10.00");
        BigDecimal price = new BigDecimal("90.00");

        BookDto bookDto = new BookDto(id, invalidIsbn, titleEs, basePrice, discountPercentage, price);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<BookDto>> violations = validator.validate(bookDto);
        assertFalse(violations.isEmpty(), "Debería haber violaciones de validación por ISBN inválido");

        boolean hasIsbnViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("isbn"));
        assertTrue(hasIsbnViolation, "Debería haber una violación de validación para el ISBN");
    }

    @Test
    void throw_exception_when_validate_with_invalid_isbn() {
        BookDto dto = new BookDto(
                1L,
                "invalidIsbn",
                "Título en Español",
                new BigDecimal("100.00"),
                new BigDecimal("10.00"),
                new BigDecimal("90.00")
        );

        assertThrows(ValidationException.class, () -> DtoValidator.validate(dto));



    }

    @Test
    void throw_exception_when_validate_with_null_base_price() {
        BookDto dto = new BookDto(
                1L,
                "9783161484100",
                "Título en Español",
                null,
                new BigDecimal("10.00"),
                new BigDecimal("90.00")
        );

        assertThrows(ValidationException.class, () -> DtoValidator.validate(dto));
    }

    @Test
    void no_exception_when_validate_with_valid_data() {
        BookDto dto = new BookDto(
                1L,
                "9783161484100",
                "Título en Español",
                new BigDecimal("100.00"),
                new BigDecimal("10.00"),
                new BigDecimal("90.00")
        );

        assertDoesNotThrow(() -> DtoValidator.validate(dto));
    }

    @Test
    void throw_exception_when_validate_with_invalid_discount_percentage() {
        BookDto dto = new BookDto(
                1L,
                "9783161484100",
                "Título en Español",
                new BigDecimal("100.00"),
                new BigDecimal("150.00"), // Porcentaje de descuento inválido
                new BigDecimal("90.00")
        );

        assertThrows(ValidationException.class, () -> DtoValidator.validate(dto));
    }
}


