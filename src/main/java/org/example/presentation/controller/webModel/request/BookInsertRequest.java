package org.example.presentation.controller.webModel.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookInsertRequest(
        String isbn,
        String titleEs,
        BigDecimal basePrice,
        BigDecimal discountPercentage
) {
}
