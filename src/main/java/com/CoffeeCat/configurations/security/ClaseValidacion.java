package com.CoffeeCat.configurations.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Getter
@Setter
public class ClaseValidacion {


    private HashMap<String, String> validacion = new HashMap<>();
}
