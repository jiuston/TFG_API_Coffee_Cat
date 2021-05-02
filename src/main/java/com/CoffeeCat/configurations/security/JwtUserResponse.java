package com.CoffeeCat.configurations.security;

import com.CoffeeCat.modelo.usuario.Rol;
import com.CoffeeCat.modelo.usuario.Usuario;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class JwtUserResponse {

    private String idUsuario;
    private String token;
    private Set<Rol> roles;

    @Builder(builderMethodName="jwtUserResponseBuilder")
    public JwtUserResponse(String idUsuario,String token, Set<Rol> roles){
        this.idUsuario=idUsuario;
        this.token=token;
        this.roles = roles;
    }


}
