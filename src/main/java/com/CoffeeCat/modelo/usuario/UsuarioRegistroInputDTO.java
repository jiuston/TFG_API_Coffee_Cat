package com.CoffeeCat.modelo.usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class UsuarioRegistroInputDTO {

    private String nombre;
    private String email;
    private String password;
    private String fecha_nacimiento;
    private String comentario_admin;

    public Usuario usuario(){
        Usuario usuario= new Usuario();
        usuario.setNombre(this.getNombre());
        usuario.setEmail(this.getEmail());
        usuario.setPassword(this.getPassword());
        try {
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(this.getFecha_nacimiento());
            usuario.setFecha_nacimiento(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        usuario.setComentario_admin(this.getComentario_admin());
        return usuario;
    }
}
