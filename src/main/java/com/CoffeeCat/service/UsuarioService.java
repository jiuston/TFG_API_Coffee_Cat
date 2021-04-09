package com.CoffeeCat.service;

import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService extends BaseService<Usuario,String, UsuarioRepository> {

    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> findByEmail(String email){return usuarioRepository.findByEmail(email);}
}
