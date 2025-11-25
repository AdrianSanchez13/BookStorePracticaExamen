package org.example.domain.service;

import org.example.domain.model.Page;
import org.example.domain.service.dto.BookDto;

import java.util.Optional;

public interface BookService {
    Page<BookDto> getAll(int page, int size);
    BookDto getByIsbn(String isbn);
    Optional<BookDto> findByIsbn(String isbn);
    BookDto create(BookDto bookDto);
    BookDto update(BookDto bookDto);
    void deleteByIsbn(String isbn);
}
