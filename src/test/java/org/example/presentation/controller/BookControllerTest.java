package org.example.presentation.controller;

import org.example.domain.model.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.example.domain.service.BookService;
import org.example.domain.service.dto.BookDto;
import org.example.presentation.controller.mapper.BookMapperPresentation;
import org.example.presentation.controller.webModel.request.BookInsertRequest;
import org.example.presentation.controller.webModel.request.BookUpdateRequest;
import org.example.presentation.controller.webModel.response.BookResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;


    @Test
    void getBookByIsbn_ShouldReturnBookResponse() throws Exception {
        // Given
        String isbn = "9780156012195";
        BookDto bookDto = new BookDto(
                1L,
                isbn,
                "El Principito",
                new BigDecimal("10.99"),
                new BigDecimal("5.00"),
                new BigDecimal("10.44")
        );

        when(bookService.getByIsbn(isbn)).thenReturn(bookDto);

        // When & Then
        mockMvc.perform(get("/api/books/{isbn}", isbn)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andExpect(jsonPath("$.titleEs").value("El Principito"))
                .andExpect(jsonPath("$.basePrice").value(10.99))
                .andExpect(jsonPath("$.discountPercentage").value(5.00));

        verify(bookService, times(1)).getByIsbn(isbn);
    }

    @Test
    void findAllBooks_ShouldReturnPageOfBookResponses() throws Exception {
        // Given
        BookDto bookDto1 = new BookDto(
                1L,
                "9780156012195",
                "El Principito",
                new BigDecimal("10.99"),
                new BigDecimal("5.00"),
                new BigDecimal("10.44")
        );

        BookDto bookDto2 = new BookDto(
                2L,
                "9788499890944",
                "Cien Años de Soledad",
                new BigDecimal("12.99"),
                new BigDecimal("10.00"),
                new BigDecimal("11.69")
        );

        List<BookDto> bookDtos = List.of(bookDto1, bookDto2);

        Page<BookDto> bookDtoPage = new Page<>(
                bookDtos,
                1,
                10,
                2L
        );

        when(bookService.getAll(1, 10)).thenReturn(bookDtoPage);

        // When & Then
        mockMvc.perform(get("/api/books")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].isbn").value("9780156012195"))
                .andExpect(jsonPath("$.data[1].isbn").value("9788499890944"))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(2L));

        verify(bookService, times(1)).getAll(1, 10);
    }

    @Test
    void createBook_ShouldReturnCreatedBookResponse() throws Exception {
        // Given
        BookInsertRequest bookInsertRequest = new BookInsertRequest(
                "9780156012195",
                "El Principito",
                new BigDecimal("10.99"),
                new BigDecimal("5.00")
        );

        BookDto createdBookDto = new BookDto(
                1L,
                "9780156012195",
                "El Principito",
                new BigDecimal("10.99"),
                new BigDecimal("5.00"),
                new BigDecimal("10.44")
        );

        when(bookService.create(org.mockito.ArgumentMatchers.any(BookDto.class))).thenReturn(createdBookDto);

        String requestBody = """
                {
                    "isbn": "9780156012195",
                    "titleEs": "El Principito",
                    "basePrice": 10.99,
                    "discountPercentage": 5.00
                }
                """;

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.isbn").value("9780156012195"))
                .andExpect(jsonPath("$.titleEs").value("El Principito"))
                .andExpect(jsonPath("$.basePrice").value(10.99))
                .andExpect(jsonPath("$.discountPercentage").value(5.00));

        verify(bookService, times(1)).create(org.mockito.ArgumentMatchers.any(BookDto.class));
    }

    @Test
    void updateBook_ShouldReturnUpdatedBookResponse() throws Exception {
        // Given
        Long bookId = 1L;
        BookUpdateRequest bookUpdateRequest = new BookUpdateRequest(
                bookId,
                "9780156012195",
                "El Principito - Updated",
                new BigDecimal("12.99"),
                new BigDecimal("10.00")
        );

        BookDto updatedBookDto = new BookDto(
                bookId,
                "9780156012195",
                "El Principito - Updated",
                new BigDecimal("12.99"),
                new BigDecimal("10.00"),
                new BigDecimal("11.69")
        );

        when(bookService.update(org.mockito.ArgumentMatchers.any(BookDto.class))).thenReturn(updatedBookDto);

        String requestBody = """
                {
                    "isbn": "9780156012195",
                    "titleEs": "El Principito - Updated",
                    "basePrice": 12.99,
                    "discountPercentage": 10.00
                }
                """;

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.isbn").value("9780156012195"))
                .andExpect(jsonPath("$.titleEs").value("El Principito - Updated"))
                .andExpect(jsonPath("$.basePrice").value(12.99))
                .andExpect(jsonPath("$.discountPercentage").value(10.00));

        verify(bookService, times(1)).update(org.mockito.ArgumentMatchers.any(BookDto.class));
    }


}