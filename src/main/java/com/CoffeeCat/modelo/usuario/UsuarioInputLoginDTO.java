package com.CoffeeCat.modelo.usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioInputLoginDTO {
    private String email;
    private String password;
}
