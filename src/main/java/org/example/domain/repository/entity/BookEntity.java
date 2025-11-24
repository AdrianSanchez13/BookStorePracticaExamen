package org.example.domain.repository.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record BookEntity(

        Long id,
        String isbn,
        String titleEs,
        BigDecimal basePrice,
        BigDecimal discountPercentage

) {

}

