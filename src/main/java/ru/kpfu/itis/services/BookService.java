package ru.kpfu.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.models.Book;
import ru.kpfu.itis.models.Image;
import ru.kpfu.itis.repositories.BookRepo;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepo bookRepo;

    @Autowired
    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }


    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }


    public Book getBookId(int id) {
        Optional<Book> optionaBook = bookRepo.findById(id);
        return optionaBook.orElse(null);
    }


    @Transactional
    public void saveBook(Book book) {
        bookRepo.save(book);
    }


    @Transactional
    public void updateBook(int id, Book book) {
        List<Image> imageList = book.getImageList();
        book.setId(id);
        book.setImageList(imageList);
        bookRepo.save(book);
    }


    @Transactional
    public void deleteBook(int id) {
        bookRepo.deleteById(id);
    }


    public Book getBookFindByTitle(Book book) {
        Optional<Book> book_db = bookRepo.findByTitle(book.getTitle());
        return book_db.orElse(null);
    }
}
