package com.kottragu.umlproject;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableVaadin
@EnableScheduling
public class UmlprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(UmlprojectApplication.class, args);
    }

}
