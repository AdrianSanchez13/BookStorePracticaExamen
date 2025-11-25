package org.example.domain.service.impl;

import org.example.domain.mapper.BookMapper;
import org.example.domain.model.Page;
import org.example.domain.repository.BookRepository;
import org.example.domain.repository.entity.BookEntity;
import org.example.domain.service.BookService;
import org.example.domain.service.dto.BookDto;

import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;


    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Page<BookDto> getAll(int page, int size) {
       if (page < 1 || size < 1) {
           throw new IllegalArgumentException("Page and size must be greater than 0");
       }
       Page<BookEntity> bookEntityPage = bookRepository.findAll(page, size);
       // Assuming a mapping function exists to convert entities to DTOs
        List<BookDto> itemsDto = bookEntityPage.data()
                .stream()
                .map(BookMapper.getInstance()::fromBookEntityToBook)
                .map(BookMapper.getInstance()::fromBookToBookDto)
                .toList();
         return new Page<>(
                    itemsDto,
                    bookEntityPage.pageNumber(),
                    bookEntityPage.pageSize(),
                    bookEntityPage.totalElements()
         );
    }

    @Override
    public BookDto getByIsbn(String isbn) {
        return bookRepository
                .findByIsbn(isbn)
                .map(BookMapper.getInstance()::fromBookEntityToBook)
                .map(BookMapper.getInstance()::fromBookToBookDto)
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));
    }

    @Override
    public Optional<BookDto> findByIsbn(String isbn) {
        return bookRepository
                .findByIsbn(isbn)
                .map(BookMapper.getInstance()::fromBookEntityToBook)
                .map(BookMapper.getInstance()::fromBookToBookDto);
    }

    @Override
    public BookDto create(BookDto bookDto) {
        if(bookRepository.findByIsbn(bookDto.isbn()).isPresent()) {
            throw new RuntimeException("Book with ISBN " + bookDto.isbn() + " already exists");
        }

        // BookDto → Book → BookEntity
        BookEntity entityToSave = BookMapper.getInstance()
                .fromBookToBookEntity(
                        BookMapper.getInstance().fromBookDtoToBook(bookDto)
                );

        BookEntity savedEntity = bookRepository.save(entityToSave);
        return BookMapper.getInstance()
                .fromBookToBookDto(
                        BookMapper.getInstance().fromBookEntityToBook(savedEntity)
                );
    }
    @Override
    public BookDto update(BookDto bookDto) {
        BookEntity existingEntity = bookRepository.findByIsbn(bookDto.isbn())
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + bookDto.isbn()));

        // Crear nueva entidad con datos actualizados (BookDto → Book → BookEntity)
        BookEntity updatedEntity = BookMapper.getInstance()
                .fromBookToBookEntity(
                        BookMapper.getInstance().fromBookDtoToBook(bookDto)
                );

        // Mantener el ID de la entidad existente
        BookEntity entityToSave = new BookEntity(
                existingEntity.id(), // ID original
                updatedEntity.isbn(),
                updatedEntity.titleEs(),
                updatedEntity.basePrice(),
                updatedEntity.discountPercentage()
        );

        BookEntity savedEntity = bookRepository.save(entityToSave);
        return BookMapper.getInstance()
                .fromBookToBookDto(
                        BookMapper.getInstance().fromBookEntityToBook(savedEntity)
                );
    }

    @Override
    public void deleteByIsbn(String isbn) {
        BookEntity existingEntity = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));
        bookRepository.deleteByIsbn(existingEntity.isbn());
    }
}
