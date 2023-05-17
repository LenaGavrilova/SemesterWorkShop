package ru.kpfu.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.exceptions.BookNotFound;
import ru.kpfu.itis.exceptions.ServerError;
import ru.kpfu.itis.repositories.BookRepo;
import ru.kpfu.itis.services.BookService;

@Controller
@RequestMapping("/book")
public class MainController {
    private final BookService bookService;
    private final BookRepo bookRepo;


    @Autowired
    public MainController(BookService bookService, BookRepo bookRepo) {
        this.bookService = bookService;
        this.bookRepo = bookRepo;
    }

    @GetMapping("")
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "/book/book";
    }

    @GetMapping("/info/{id}")
    public String infoBook(@PathVariable("id") int id, Model model) throws BookNotFound {
        if (bookService.getBookId(id) == null) {
            throw new BookNotFound();
        }
        model.addAttribute("book", bookService.getBookId(id));
        return "/book/infoBook";
    }

    @PostMapping("/search")
    public String productSearch(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "min", required = false) String min, @RequestParam(value = "max", required = false) String max, @RequestParam(value = "price", required = false, defaultValue = "asc_sort") String price, @RequestParam(value = "category", required = false) String category, Model model) throws ServerError {

        if (search.isEmpty() & min.isEmpty() & max.isEmpty() & !category.isEmpty()) {
            switch (category) {
                case "Роман":
                    model.addAttribute("search_book", bookRepo.findBookByCategory(0));
                    break;
                case "Детектив":
                    model.addAttribute("search_book", bookRepo.findBookByCategory(1));
                    break;
                case "Фантастика":
                    model.addAttribute("search_book", bookRepo.findBookByCategory(2));
                    break;
                case "Научная литература":
                    model.addAttribute("search_book", bookRepo.findBookByCategory(3));
                    break;
                case "Мистика":
                    model.addAttribute("search_book", bookRepo.findBookByCategory(4));
                    break;
            }
        } else if (!search.isEmpty() & !min.isEmpty() & !max.isEmpty() & !price.isEmpty() & !category.isEmpty()) {
            if (price.equals("asc_sort")) {
                switch (category) {
                    case "Роман":
                        model.addAttribute("search_book",
                                bookRepo.findBookByPartTitleAndCategoryAndPriceOrderByPriceAsc(search, Float.parseFloat(min), Float.parseFloat(max), 0));
                        break;
                    case "Детектив":
                        model.addAttribute("search_book",
                                bookRepo.findBookByPartTitleAndCategoryAndPriceOrderByPriceAsc(search, Float.parseFloat(min), Float.parseFloat(max), 1));
                        break;
                    case "Фантастика":
                        model.addAttribute("search_book",
                                bookRepo.findBookByPartTitleAndCategoryAndPriceOrderByPriceAsc(search, Float.parseFloat(min), Float.parseFloat(max), 2));
                        break;
                    case "Научная литература":
                        model.addAttribute("search_book",
                                bookRepo.findBookByPartTitleAndCategoryAndPriceOrderByPriceAsc(search, Float.parseFloat(min), Float.parseFloat(max), 3));
                        break;
                    case "Мистика":
                        model.addAttribute("search_book",
                                bookRepo.findBookByPartTitleAndCategoryAndPriceOrderByPriceAsc(search, Float.parseFloat(min), Float.parseFloat(max), 4));
                        break;
                }
            } else if (price.equals("desc_sort")) {
                switch (category) {
                    case "Роман":
                        model.addAttribute("search_book",
                                bookRepo.findBookByPartTitleAndCategoryAndPriceOrderByPriceDesc(search, Float.parseFloat(min), Float.parseFloat(max), 0));
                        break;
                    case "Детектив":
                        model.addAttribute("search_book",
                                bookRepo.findBookByPartTitleAndCategoryAndPriceOrderByPriceDesc(search, Float.parseFloat(min), Float.parseFloat(max), 1));
                        break;
                    case "Фантастика":
                        model.addAttribute("search_book",
                                bookRepo.findBookByPartTitleAndCategoryAndPriceOrderByPriceDesc(search, Float.parseFloat(min), Float.parseFloat(max), 2));
                        break;
                    case "Научная литература":
                        model.addAttribute("search_book",
                                bookRepo.findBookByPartTitleAndCategoryAndPriceOrderByPriceDesc(search, Float.parseFloat(min), Float.parseFloat(max), 3));
                        break;
                    case "Мистика":
                        model.addAttribute("search_book",
                                bookRepo.findBookByPartTitleAndCategoryAndPriceOrderByPriceDesc(search, Float.parseFloat(min), Float.parseFloat(max), 4));
                        break;
                }
            }
        } else if (search.isEmpty() & !min.isEmpty() & !max.isEmpty() & !category.isEmpty() & !price.isEmpty()) {
            if (price.equals("asc_sort")) {
                switch (category) {
                    case "Роман":
                        model.addAttribute("search_book",
                                bookRepo.findBookByCategoryOrderByPriceAcs(0, Float.parseFloat(min), Float.parseFloat(max)));
                        break;
                    case "Детектив":
                        model.addAttribute("search_book",
                                bookRepo.findBookByCategoryOrderByPriceAcs(1, Float.parseFloat(min), Float.parseFloat(max)));
                        break;
                    case "Фантастика":
                        model.addAttribute("search_book",
                                bookRepo.findBookByCategoryOrderByPriceAcs(2, Float.parseFloat(min), Float.parseFloat(max)));
                        break;
                    case "Научная литература":
                        model.addAttribute("search_book",
                                bookRepo.findBookByCategoryOrderByPriceAcs(3, Float.parseFloat(min), Float.parseFloat(max)));
                        break;
                    case "Мистика":
                        model.addAttribute("search_book",
                                bookRepo.findBookByCategoryOrderByPriceAcs(4, Float.parseFloat(min), Float.parseFloat(max)));
                        break;
                }
            } else if (price.equals("desc_sort")) {
                switch (category) {
                    case "Роман":
                        model.addAttribute("search_book",
                                bookRepo.findBookByCategoryOrderByPriceDesc(0, Float.parseFloat(min), Float.parseFloat(max)));
                        break;
                    case "Детектив":
                        model.addAttribute("search_book",
                                bookRepo.findBookByCategoryOrderByPriceDesc(1, Float.parseFloat(min), Float.parseFloat(max)));
                        break;
                    case "Фантастика":
                        model.addAttribute("search_book",
                                bookRepo.findBookByCategoryOrderByPriceDesc(2, Float.parseFloat(min), Float.parseFloat(max)));
                        break;
                    case "Научная литература":
                        model.addAttribute("search_book",
                                bookRepo.findBookByCategoryOrderByPriceDesc(3, Float.parseFloat(min), Float.parseFloat(max)));
                        break;
                    case "Мистика":
                        model.addAttribute("search_book",
                                bookRepo.findBookByCategoryOrderByPriceDesc(4, Float.parseFloat(min), Float.parseFloat(max)));
                        break;
                }
            }
        } else if (!search.isEmpty() & min.isEmpty() & max.isEmpty() & !category.isEmpty() & !price.isEmpty()) {
            if (price.equals("asc_sort")) {
                model.addAttribute("search_book",
                        bookRepo.findBookByPartTitleAndCategoryOrderByPriceAsc(search, category));
            } else if (price.equals("desc_sort")) {
                model.addAttribute("search_book",
                        bookRepo.findBookByPartTitleAndCategoryOrderByPriceDesc(search, category));
            }
        }
        model.addAttribute("value_search", search);
        model.addAttribute("value_min", min);
        model.addAttribute("value_max", max);
        model.addAttribute("value_price", price);
        model.addAttribute("value_category", category);
        return "/book/book";
    }


}
