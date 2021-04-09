package com.CoffeeCat.service;

import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends BaseService<Usuario,String, UsuarioRepository> {
}
