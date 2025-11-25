package org.example.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

   @Test
   @DisplayName("dandole un descuento del 15% a un libro de 100, el precio calculado debe ser 85")
    void givenBookWithDiscountPercentage_whenCalculatePrice_thenReturnCorrectPrice() {
        // Given
        Book book = new Book(
                1L,
                "1234567890",
                "Test Book",
                new BigDecimal("100.00"),
                new BigDecimal("15.00")
        );

        // When
        BigDecimal price = book.calculateFinalPrice();

        // Then
        assertEquals(new BigDecimal("85.00"), price);
    }


    @ParameterizedTest
    @CsvSource({
            "0, 100.00, 100.00",
            "50, 200.00, 100.00",
            "100, 150.00, 0.00",
            "25, 80.00, 60.00"
    })
    @DisplayName("Calcular precio final con diferentes porcentajes de descuento")
    void givenVariousDiscountPercentages_whenCalculatePrice_thenReturnExpectedPrice(
            String discountPercentageStr,
            String basePriceStr,
            String expectedPriceStr) {
        // Given
        BigDecimal discountPercentage = new BigDecimal(discountPercentageStr);
        BigDecimal basePrice = new BigDecimal(basePriceStr);
        BigDecimal expectedPrice = new BigDecimal(expectedPriceStr);
        Book book = new Book(
                1L,
                "1234567890",
                "Test Book",
                basePrice,
                discountPercentage
        );

        // When
        BigDecimal price = book.calculateFinalPrice();
        // Then
        assertEquals(expectedPrice.setScale(2, RoundingMode.HALF_UP), price);
    }

}