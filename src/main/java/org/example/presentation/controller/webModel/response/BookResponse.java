package org.example.presentation.controller.webModel.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record BookResponse(
        Long id,
        String isbn,
        String titleEs,
        BigDecimal basePrice,
        BigDecimal discountPercentage,
        BigDecimal price
) {
}
