package com.CoffeeCat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controler {

    @GetMapping("/")
    public String hola(){
        return "hola";
    }

}
