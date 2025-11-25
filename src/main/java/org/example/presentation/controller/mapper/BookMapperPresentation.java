package org.example.presentation.controller.mapper;


import org.example.domain.service.dto.BookDto;
import org.example.presentation.controller.webModel.request.BookInsertRequest;
import org.example.presentation.controller.webModel.request.BookUpdateRequest;
import org.example.presentation.controller.webModel.response.BookResponse;

public class BookMapperPresentation {


    private static BookMapperPresentation INSTANCE;

    private BookMapperPresentation() {
    }

    public static BookMapperPresentation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookMapperPresentation();
        }
        return INSTANCE;
    }

    public static BookResponse fromBookDtoToBookResponse(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }

        return new BookResponse(
                bookDto.id(),
                bookDto.isbn(),
                bookDto.titleEs(),
                bookDto.basePrice(),
                bookDto.discountPercentage(),
                bookDto.price()

        );
    }

    public static BookDto fromBookInsertToBookDto(BookInsertRequest bookinsert) {
        if (bookinsert == null) {
            return null;
        }
        return new BookDto(
                null,
                bookinsert.isbn(),
                bookinsert.titleEs(),
                bookinsert.basePrice(),
                bookinsert.discountPercentage(),
                null
        );
    }

    public static BookDto fromBookUpdateToBookDto(BookUpdateRequest bookupdate) {
        if (bookupdate == null) {
            return null;
        }
        return new BookDto(
                null,
                bookupdate.isbn(),
                bookupdate.titleEs(),
                bookupdate.basePrice(),
                bookupdate.discountPercentage(),
                null
        );
    }
}