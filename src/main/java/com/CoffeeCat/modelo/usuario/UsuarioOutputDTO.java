package com.CoffeeCat.modelo.usuario;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Set;

@Data
@NoArgsConstructor
public class UsuarioOutputDTO {

    private String id;
    private String nombre;
    private String email;
    private Set<Rol> roles;
    private String comentario_admin;

    public UsuarioOutputDTO(Usuario usuario){
        if (usuario==null) return;
        this.setId(usuario.getId());
        this.setNombre(usuario.getNombre());
        this.setEmail(usuario.getEmail());
        this.setRoles(usuario.getRoles());
        this.setComentario_admin(usuario.getComentario_admin());
    }

}
