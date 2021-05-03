package com.CoffeeCat.modelo.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPass {
    private String email;
    private String token;
    private String pass;
    private String repitePass;
}
