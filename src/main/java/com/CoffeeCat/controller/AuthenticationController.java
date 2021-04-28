package com.CoffeeCat.controller;

import com.CoffeeCat.configurations.security.JwtTokenProvider;
import com.CoffeeCat.configurations.security.JwtUserResponse;
import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.modelo.usuario.UsuarioInputLoginDTO;
import com.CoffeeCat.modelo.usuario.UsuarioOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public JwtUserResponse login(@RequestBody UsuarioInputLoginDTO inputLoginDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(inputLoginDTO.getEmail(), inputLoginDTO.getContraseña()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String jwtToken = tokenProvider.generateToken(authentication);

        return convertUserEntityAndTokenToJwtUserResponse(usuario, jwtToken);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/usuario/me")
    public UsuarioOutputDTO me(@AuthenticationPrincipal Usuario usuario){
        return new UsuarioOutputDTO(usuario);
    }
    private JwtUserResponse convertUserEntityAndTokenToJwtUserResponse(Usuario user, String jwtToken) {
        return JwtUserResponse
                .jwtUserResponseBuilder()
                .email(user.getEmail())
                .roles(user.getRoles())
                .token(jwtToken)
                .build();
    }

}