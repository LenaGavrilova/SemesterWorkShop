package ru.kpfu.itis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.kpfu.itis.config.Config;
import ru.kpfu.itis.config.SecurityConfig;
import ru.kpfu.itis.config.WebConfig;

@SpringBootApplication
@Import({Config.class, SecurityConfig.class, WebConfig.class})

public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }
}
