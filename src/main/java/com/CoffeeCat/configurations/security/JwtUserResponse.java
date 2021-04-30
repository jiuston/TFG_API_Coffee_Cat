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

    private String token;
    private Set<Rol> roles;

    @Builder(builderMethodName="jwtUserResponseBuilder")
    public JwtUserResponse(String token, Set<Rol> roles){
        this.token=token;
        this.roles = roles;
    }


}
