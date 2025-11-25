package org.example.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Book {
    private final Long id;
    private final String isbn;
    private final String titleEs;
    private final BigDecimal basePrice;
    private final BigDecimal discountPercentage;
    private final BigDecimal price;



    public Book(Long id, String isbn, String titleEs, BigDecimal basePrice, BigDecimal discountPercentage) {
        this.id = id;
        this.isbn = isbn;
        this.titleEs = titleEs;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
        this.price = calculateFinalPrice();

    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitleEs() {
        return titleEs;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }



    public BigDecimal calculateFinalPrice() {
        if( basePrice == null ) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal discount = basePrice
                .multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return basePrice.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }




}
