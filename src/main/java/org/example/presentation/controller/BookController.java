package org.example.presentation.controller;

import org.example.domain.model.Page;
import org.example.domain.service.BookService;
import org.example.domain.service.dto.BookDto;
import org.example.presentation.controller.mapper.BookMapperPresentation;
import org.example.presentation.controller.webModel.request.BookInsertRequest;
import org.example.presentation.controller.webModel.request.BookUpdateRequest;
import org.example.presentation.controller.webModel.response.BookResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> findAllBooks(
            @RequestParam (required = false, defaultValue = "1") int page,
            @RequestParam (required = false, defaultValue = "10") int size) {

        Page<BookDto> bookDtoPage = bookService.getAll(page, size);

        List<BookResponse> bookSummaries = bookDtoPage.data().stream()
                .map(BookMapperPresentation::fromBookDtoToBookResponse)
                .toList();

        Page<BookResponse> bookSummaryPage = new Page<>(
                bookSummaries,
                bookDtoPage.pageNumber(),
                bookDtoPage.pageSize(),
                bookDtoPage.totalElements()
        );
        return new ResponseEntity<>(bookSummaryPage, HttpStatus.OK);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(@PathVariable String isbn) {
        BookDto bookDto = bookService.getByIsbn(isbn);
        BookResponse bookResponse = BookMapperPresentation.fromBookDtoToBookResponse(bookDto);
        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody BookInsertRequest bookInsertRequest) {
        BookDto bookDto = BookMapperPresentation.fromBookInsertToBookDto(bookInsertRequest);
        BookDto createdBook = bookService.create(bookDto);
        BookResponse bookResponse = BookMapperPresentation.fromBookDtoToBookResponse(createdBook);
        return new ResponseEntity<>(bookResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")

    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @RequestBody BookUpdateRequest bookUpdateRequest) {
        BookDto bookDto = BookMapperPresentation.fromBookUpdateToBookDto(bookUpdateRequest);
        BookDto updatedBook = bookService.update(bookDto);
        BookResponse bookResponse = BookMapperPresentation.fromBookDtoToBookResponse(updatedBook);
        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable("isbn") String isbn) {
        bookService.deleteByIsbn(isbn);
        return ResponseEntity.noContent().build();
    }
}
