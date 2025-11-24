package org.example.domain.service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BookDto(
        @NotNull
        Long id,
        @NotNull
        @Size(min = 13, max = 13, message = "El ISBN debe tener exactamente 13 caracteres")
        String isbn,
        @NotBlank(message = "El título en español no puede estar vacío")
        String titleEs,
        @NotNull(message = "El precio base no puede ser nulo")
        @DecimalMin(value = "0.0", inclusive = true, message = "El precio base debe ser mayor o igual a 0")
        BigDecimal basePrice,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = true, message = "El descuento no puede ser menor a 0")
        @DecimalMax(value = "100.0", inclusive = true, message = "El descuento no puede ser mayor a 100")
        BigDecimal discountPercentage,
        BigDecimal price
) {

}
