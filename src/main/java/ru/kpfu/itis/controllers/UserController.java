package ru.kpfu.itis.controllers;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.exceptions.BookNotFound;
import ru.kpfu.itis.exceptions.DefaultError;
import ru.kpfu.itis.exceptions.ServerError;
import ru.kpfu.itis.exceptions.UserNotFound;
import ru.kpfu.itis.models.Book;
import ru.kpfu.itis.models.Cart;
import ru.kpfu.itis.models.Order;
import ru.kpfu.itis.models.Status;
import ru.kpfu.itis.repositories.BookRepo;
import ru.kpfu.itis.repositories.CartRepo;
import ru.kpfu.itis.repositories.OrderRepo;
import ru.kpfu.itis.services.BookService;
import ru.kpfu.itis.services.UserDetail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
public class UserController {
    private final BookRepo bookRepo;

    private final OrderRepo orderRepo;
    private final BookService bookService;
    private final CartRepo cartRepo;


    @Autowired
    public UserController(BookRepo bookRepo, OrderRepo orderRepo, BookService bookService, CartRepo cartRepo) {
        this.bookRepo = bookRepo;
        this.orderRepo = orderRepo;
        this.bookService = bookService;
        this.cartRepo = cartRepo;
    }


    @GetMapping("/index")
    public String index(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetail userDetail = (UserDetail) authentication.getPrincipal();

        String role = userDetail.getUser().getRole();

        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin";
        }
        model.addAttribute("books", bookService.getAllBooks());
        return "/user/index";
    }

    @GetMapping("/cart/{currency}")
    public String cart(Model model, @PathVariable(value = "currency", required = false) String currency) throws UserNotFound, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        if (userDetail.getUser() == null) {
            throw new UserNotFound();
        }

        int id_user = userDetail.getUser().getId();

        List<Cart> cartList = cartRepo.findByUserId((id_user));
        List<Book> bookList = new ArrayList<>();
        for (Cart cart : cartList
        ) {
            bookList.add(bookService.getBookId(cart.getBookId()));
        }

        float price = 0;
        for (Book book : bookList
        ) {
            price += book.getPrice();
        }
        model.addAttribute("price", price);
        model.addAttribute("cart_book", bookList);
        if (currency != null) {
            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://openexchangerates.org/api/latest.json?app_id=b1f712f1977e4b1eb802159f2974c461")
                    .build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);
                JSONObject jsonResponse = new JSONObject(Objects.requireNonNull(response.body()).string());
                double exchangeRate = jsonResponse.getJSONObject("rates").getDouble(currency);
                double priceInSelectedCurrency = price * exchangeRate;
                model.addAttribute("price", priceInSelectedCurrency);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "/user/cart";
    }

    @GetMapping("/info/{id}")
    public String infoBook(@PathVariable("id") int id, Model model) throws BookNotFound {
        if (bookService.getBookId(id) == null) {
            throw new BookNotFound();
        }
        model.addAttribute("book", bookService.getBookId(id));
        return "/book/infoBook";
    }

    @GetMapping("/cart/add/{id}")
    public String addBookInCart(@PathVariable("id") int id) throws BookNotFound, UserNotFound {
        Book book = bookService.getBookId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        if (userDetail.getUser() == null) {
            throw new UserNotFound();
        }
        if (bookService.getBookId(id) == null) {
            throw new BookNotFound();
        }
        int id_user = userDetail.getUser().getId();
        Cart cart = new Cart(id_user, book.getId());
        cartRepo.save(cart);
        return "redirect:/cart/USD";
    }

    @GetMapping("/cart/delete/{id}")
    public String deleteProductInCart(@PathVariable("id") int id) throws BookNotFound, UserNotFound {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        if (userDetail.getUser() == null) {
            throw new UserNotFound();
        }
        if (bookService.getBookId(id) == null) {
            throw new BookNotFound();
        }
        int id_user = userDetail.getUser().getId();

        cartRepo.deleteCartById(id, id_user);
        return "redirect:/cart/USD";
    }

    @GetMapping("/order/create")
    public String createOrder() throws UserNotFound, BookNotFound {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        if (userDetail.getUser() == null) {
            throw new UserNotFound();
        }
        int id_user = userDetail.getUser().getId();

        List<Cart> cartList = cartRepo.findByUserId((id_user));
        List<Book> bookList = new ArrayList<>();
        for (Cart cart : cartList
        ) {
            if (bookService.getBookId(cart.getBookId()) == null) {
                throw new BookNotFound();
            }
            bookList.add(bookService.getBookId(cart.getBookId()));
        }

        String uuid = UUID.randomUUID().toString();

        for (Book book : bookList
        ) {
            Order newOrder = new Order(uuid, 1, book.getPrice(), Status.Оформлен, book, userDetail.getUser());
            orderRepo.save(newOrder);
            cartRepo.deleteCartById(book.getId(), id_user);
        }

        return "redirect:/orders";

    }

    @GetMapping("/orders")
    public String ordersUser(Model model) throws UserNotFound {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        if (userDetail.getUser() == null) {
            throw new UserNotFound();
        }
        List<Order> orderList = orderRepo.findByUser((userDetail.getUser()));
        model.addAttribute("orders", orderList);
        return "/user/orders";
    }

    @PostMapping("index/search")
    public String productSearch(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "min", required = false) String min, @RequestParam(value = "max", required = false) String max, @RequestParam(value = "price", required = false, defaultValue = "asc_sort") String price, @RequestParam(value = "category", required = false) String category, Model model) {

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
        return "/user/index";
    }
}
