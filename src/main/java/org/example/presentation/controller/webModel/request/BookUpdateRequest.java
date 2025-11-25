package org.example.presentation.controller.webModel.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookUpdateRequest(
        Long id,
        String isbn,
        String titleEs,
        BigDecimal basePrice,
        BigDecimal discountPercentage
) {
}
