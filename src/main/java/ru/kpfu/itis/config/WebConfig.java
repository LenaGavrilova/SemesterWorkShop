package ru.kpfu.itis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.kpfu.itis.models.Book;
import ru.kpfu.itis.models.User;
import ru.kpfu.itis.utils.StringToEntityConverter;

@Configuration
@ComponentScan("ru.kpfu.itis.controllers")
public class WebConfig implements WebMvcConfigurer {
    @Value("${uploadPath}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/img/**").addResourceLocations("file://" + uploadPath + "/"); // извлечение из директории uploadPath
        registry.addResourceHandler("/js/**").addResourceLocations("/assets/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("/assets/css/");

    }


    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {
        formatterRegistry.addConverter(bookGenericConverter());
        formatterRegistry.addConverter(userGenericConverter());

    }

    @Bean
    public StringToEntityConverter bookGenericConverter() {
        return new StringToEntityConverter(Book.class);
    }

    @Bean
    public StringToEntityConverter userGenericConverter() {
        return new StringToEntityConverter(User.class);
    }


}
