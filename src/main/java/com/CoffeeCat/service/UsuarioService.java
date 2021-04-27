package com.CoffeeCat.service;

import com.CoffeeCat.modelo.usuario.Rol;
import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UsuarioService extends BaseService<Usuario,String, UsuarioRepository> {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    public Optional<Usuario> findByEmail(String email){return usuarioRepository.findByEmail(email);}

    //Metodo para registrar usuarios.
    public Usuario createUsuario(Usuario usuario){
        Set<Rol> roles = new HashSet<Rol>();
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        if (usuario.getRoles().size() == 0) {
            roles.add(Rol.USER);
            usuario.setRoles(roles);
        }
        return usuarioRepository.save(usuario);
    }
}
