package com.CoffeeCat.modelo.usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@NoArgsConstructor
public class UsuarioRegistroInputDTO {

    private String nombre;
    private String email;
    private String password;

    public Usuario usuario(){
        Usuario usuario= new Usuario();
        usuario.setNombre(this.getNombre());
        usuario.setEmail(this.getEmail());
        usuario.setPassword(this.getPassword());
        usuario.setRoles(new HashSet<Rol>());
        return usuario;
    }
}
