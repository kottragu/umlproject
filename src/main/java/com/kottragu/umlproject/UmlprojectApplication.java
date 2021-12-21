package com.kottragu.umlproject;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableVaadin
public class UmlprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(UmlprojectApplication.class, args);
    }

}
