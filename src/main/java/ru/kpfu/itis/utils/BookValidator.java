package ru.kpfu.itis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kpfu.itis.models.Book;
import ru.kpfu.itis.services.BookService;

@Component
public class BookValidator implements Validator {


    private final BookService bookService;

    @Autowired
    public BookValidator(BookService boorService) {
        this.bookService = boorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        if (bookService.getBookFindByTitle(book) != null) {
            errors.rejectValue("title", "", "Данное название товара уже используется");
        }
    }
}