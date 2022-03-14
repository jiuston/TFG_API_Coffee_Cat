package com.CoffeeCat.modelo.usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;

@Data
@NoArgsConstructor
public class UsuarioRegistroInputDTO {
    @NotBlank(message="First name cannot be missing or empty")
    private String nombre;
    @Email
    private String email;
    @NotNull(message="Password is a required field")
    @Size(min=5, max=16, message="Password must be equal to or greater than 5 characters and less than 16 characters")
    private String password;

    public Usuario usuario(){
        Usuario usuario= new Usuario();
        usuario.setNombre(this.getNombre());
        usuario.setEmail(this.getEmail());
        usuario.setPassword(this.getPassword());
        usuario.setRoles(new HashSet<>());
        return usuario;
    }
}
