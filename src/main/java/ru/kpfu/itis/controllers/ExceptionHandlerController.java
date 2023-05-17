package ru.kpfu.itis.controllers;

import javax.servlet.http.HttpServletRequest;

import org.attoparser.ParseException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.exceptions.TemplateProcessingException;
import ru.kpfu.itis.exceptions.BookNotFound;
import ru.kpfu.itis.exceptions.DefaultError;
import ru.kpfu.itis.exceptions.ServerError;
import ru.kpfu.itis.exceptions.UserNotFound;

import java.io.IOException;

@Controller
@ControllerAdvice
public class ExceptionHandlerController implements ErrorController {

    private static final String PATH = "/error";


    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView errorHandler(HttpServletRequest req, Exception exception) {
        ModelAndView modelAndView = new ModelAndView("/error/404");
        modelAndView.addObject("errorTitle", "This page is not constructed!");
        modelAndView.addObject("errorDescription", "The Page you are looking for is not available now!");
        modelAndView.addObject("title", "404 Error Page");
        modelAndView.addObject("exception", exception);
        modelAndView.addObject("url", req.getRequestURL());
        return modelAndView;
    }


    public String getErrorPath() {
        return PATH;
    }

    @ExceptionHandler(BookNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView errorBookNotfound(HttpServletRequest req, Exception exception) {
        ModelAndView modelAndView = new ModelAndView("/error/404");
        modelAndView.addObject("errorTitle", "This Product Not Available");
        modelAndView.addObject("errorDescription", "The Product you looking for is not available right Now!");
        modelAndView.addObject("title", "Product Unavailable");
        modelAndView.addObject("exception", exception);
        modelAndView.addObject("url", req.getRequestURL());
        return modelAndView;
    }

    @ExceptionHandler({MultipartException.class})
    public String hand(MultipartException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "/redirect:/admin/add";

    }

    @ExceptionHandler({ServerError.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView errorServer(HttpServletRequest req, Exception exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("/error/500");
        return mav;
    }

    @ExceptionHandler({DefaultError.class, NullPointerException.class, IllegalStateException.class,
            TemplateProcessingException.class, ParseException.class, TemplateInputException.class,IOException.class})

    public ModelAndView errorDefault(HttpServletRequest req, Exception exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("/error/default");
        return mav;
    }

    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView errorUserNotFound(HttpServletRequest req, Exception exception) {
        ModelAndView modelAndView = new ModelAndView("/error/404");
        modelAndView.addObject("errorTitle", "This user is not found!");
        modelAndView.addObject("errorDescription", "This user is not registered in this site!");
        modelAndView.addObject("title", "404 Error Page");
        modelAndView.addObject("exception", exception);
        modelAndView.addObject("url", req.getRequestURL());
        return modelAndView;
    }
}
