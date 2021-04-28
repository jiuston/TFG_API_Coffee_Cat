package com.CoffeeCat.configurations.security;

import com.CoffeeCat.modelo.usuario.Rol;
import com.CoffeeCat.modelo.usuario.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    // cabecera utilizada para mandar el token
    public static final String TOKEN_HEADER = "Authorization";
    // prefijo del token en la cabecera
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    @Value("${jwt.secret:EnUnLugarDeLaManchaDeCuyoNombreNoQuieroAcordarmeNoHaMuchoTiempoQueViviaUnHidalgo}")
    private String jwtSecreto;
    @Value("${jwt.token-expiration:1800000}") // milisegundos
    private int jwtDuracionToken;

    public String generateToken(Authentication authentication) {

        Usuario usuario= (Usuario) authentication.getPrincipal();
        Date tokenExpirationDate= new Date(System.currentTimeMillis()+jwtDuracionToken);
        return Jwts.builder().signWith(Keys.hmacShaKeyFor(jwtSecreto.getBytes()), SignatureAlgorithm.HS512).setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(usuario.getId()).setIssuedAt(new Date()).setExpiration(tokenExpirationDate)
                .claim("email", usuario.getEmail()).claim("roles",usuario.getRoles().stream().map(Rol::name).collect(Collectors.joining(", "))).compact();
    }

    public String getUserId(String token){
        Claims claims=Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(jwtSecreto.getBytes())).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken){
        try {
            Jwts.parser().setSigningKey(jwtSecreto.getBytes()).parseClaimsJws(authToken);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
