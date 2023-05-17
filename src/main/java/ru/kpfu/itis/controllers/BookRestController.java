package ru.kpfu.itis.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.exceptions.BookNotFound;
import ru.kpfu.itis.models.Book;
import ru.kpfu.itis.repositories.BookRepo;
import ru.kpfu.itis.services.BookService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Book Controller", description = "REST API for Book Entity")
@RequestMapping("/api/books")
public class BookRestController {

    private final BookRepo bookRepo;
    private final BookService bookService;

    @Operation(summary = "Get all books")
    @GetMapping()
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    @Operation(summary = "Get book by id")
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable int id) throws BookNotFound {
        return bookRepo.findById(id).orElseThrow(BookNotFound::new);
    }


    @Operation(summary = "Create a new book")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("")
    public Book createBook(@Valid @RequestBody Book book) {
        return bookRepo.save(book);
    }

    @Operation(summary = "Update an existing book")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable int id, @Valid @RequestBody Book bookDetails) throws BookNotFound {
        Book book = bookRepo.findById(id).orElseThrow(BookNotFound::new);
        book.setTitle(bookDetails.getTitle());
        book.setDescription(bookDetails.getDescription());
        book.setPrice(bookDetails.getPrice());
        book.setPublishingHouse(bookDetails.getPublishingHouse());
        book.setCategory(bookDetails.getCategory());
        book.setDataTimeOfCreated(bookDetails.getDataTimeOfCreated());
        return bookRepo.save(book);
    }

    @Operation(summary = "Delete a book by id")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable int id) throws BookNotFound {
        Book book = bookRepo.findById(id).orElseThrow(BookNotFound::new);
        bookRepo.delete(book);
        return ResponseEntity.ok().build();
    }
}
