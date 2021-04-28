package com.CoffeeCat.configurations.security;

import com.CoffeeCat.modelo.usuario.Rol;
import com.CoffeeCat.modelo.usuario.Usuario;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class JwtUserResponse extends Usuario {

    private String token;

    @Builder(builderMethodName="jwtUserResponseBuilder")
    public JwtUserResponse(String email, Set<Rol> roles, String token){
        this.setEmail(email);
        this.setRoles(roles);
        this.token=token;
    }


}
