package ru.kpfu.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.exceptions.BookNotFound;
import ru.kpfu.itis.exceptions.DefaultError;
import ru.kpfu.itis.models.*;
import ru.kpfu.itis.repositories.OrderRepo;
import ru.kpfu.itis.repositories.UserRepo;
import ru.kpfu.itis.repositories.CategoryRepo;
import ru.kpfu.itis.services.BookService;
import ru.kpfu.itis.services.OrderService;
import ru.kpfu.itis.services.UserDetail;
import ru.kpfu.itis.services.UserService;
import ru.kpfu.itis.utils.BookValidator;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${uploadPath}")
    private String uploadPath;

    private final BookValidator bookValidator;

    private final BookService bookService;

    private final CategoryRepo categoryRepo;
    private final OrderService orderService;

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final UserService userService;

    @Autowired
    public AdminController(BookValidator bookValidator, BookService bookService, CategoryRepo categoryRepo, OrderService orderService, OrderRepo orderRepo, UserRepo userRepo, UserService userService) {
        this.bookValidator = bookValidator;
        this.bookService = bookService;
        this.categoryRepo = categoryRepo;
        this.orderService = orderService;
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @GetMapping()
    public String admin(Model model) throws BookNotFound {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetails = (UserDetail) authentication.getPrincipal();
        String role = userDetails.getUser().getRole();

        if (role.equals("ROLE_USER")) {
            return "redirect:/index";
        }
        model.addAttribute("books", bookService.getAllBooks());
        return "/admin/admin";
    }

    @GetMapping("book/add")
    public String addBook(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("category", categoryRepo.findAll());
        return "/book/addBook";
    }


    @PostMapping("/book/add")
    public String addBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @RequestParam("file_one") MultipartFile file_one, @RequestParam("file_two") MultipartFile file_two, @RequestParam("file_three") MultipartFile file_three, @RequestParam("file_four") MultipartFile file_four, @RequestParam("file_five") MultipartFile file_five) throws MultipartException, IOException {

        if (bindingResult.hasErrors()) {
            return "book/addBook";
        }
        bookValidator.validate(book, bindingResult);

        if (file_one != null) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {

                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setBook(book);
            image.setFileName(resultFileName);
            book.addImageBook(image);

        }

        if (file_two != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setBook(book);
            image.setFileName(resultFileName);
            book.addImageBook(image);

        }

        if (file_three != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setBook(book);
            image.setFileName(resultFileName);
            book.addImageBook(image);

        }

        if (file_four != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setBook(book);
            image.setFileName(resultFileName);
            book.addImageBook(image);

        }

        if (file_five != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five.getOriginalFilename();
            file_five.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setBook(book);
            image.setFileName(resultFileName);
            book.addImageBook(image);

        }

        book.setDataTimeOfCreated(LocalDateTime.now());
        bookService.saveBook(book);
        return "redirect:/admin";
    }

    @GetMapping("book/edit/{id}")
    public String editBook(@PathVariable("id") int id, Model model) throws BookNotFound {
        if (bookService.getBookId(id) == null) {
            throw new BookNotFound();
        }
        model.addAttribute("editBook", bookService.getBookId(id));
        model.addAttribute("category", categoryRepo.findAll());
        return "/book/editBook";
    }


    @PostMapping("/book/edit/{id}")
    public String editBook(@ModelAttribute("editBook") Book book, @PathVariable("id") int id) throws BookNotFound {
        if (bookService.getBookId(id) == null) {
            throw new BookNotFound();
        }
        book.setDataTimeOfCreated(LocalDateTime.now());
        bookService.updateBook(id, book);
        return "redirect:/admin";
    }

    @GetMapping("/editStatus/{id}")
    public String editStatus(@PathVariable("id") int id, Model model) throws DefaultError {
        if (orderService.getOrderId(id) == null) {
            throw new DefaultError();
        }
        model.addAttribute("editStatus", orderService.getOrderId(id));
        model.addAttribute("status", Status.values());
        return "/admin/editStatus";
    }

    @PostMapping("/editStatus/{id}")
    public String editStatus(@ModelAttribute("editStatus") Order order, @PathVariable("id") int id) throws DefaultError {
        if (orderService.getOrderId(id) == null) {
            throw new DefaultError();
        }
        orderService.updateOrderStatus(id, order);
        return "redirect:/admin/orderList";
    }


    @GetMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable("id") int id) throws BookNotFound {
        if (bookService.getBookId(id) == null) {
            throw new BookNotFound();
        }
        bookService.deleteBook(id);
        return "redirect:/admin";
    }


    @GetMapping("orderList")
    public String ordersUsers(Model model) {
        model.addAttribute("ordersList", orderRepo.findAll());
        return "/admin/orderList";
    }


    @GetMapping("userList")
    public String userList(Model model) {
        model.addAttribute("userList", userRepo.findAll());
        return "/admin/userList";
    }

}

