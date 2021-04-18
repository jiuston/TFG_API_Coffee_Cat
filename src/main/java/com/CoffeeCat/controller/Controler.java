package com.CoffeeCat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class Controler {

    @GetMapping("/")
    public void hola(HttpServletResponse response) throws IOException {
        response.sendRedirect("swagger-ui.html#/");
    }

}
