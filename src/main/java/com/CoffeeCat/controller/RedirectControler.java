package com.CoffeeCat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Api(tags = "Redirect")
public class RedirectControler {

    @GetMapping("/")
    @ApiOperation("Redireccion a la pagina de Swagger")
    public void redirectToSwagger(HttpServletResponse response) throws IOException {
        response.sendRedirect("swagger-ui/");
    }

}
