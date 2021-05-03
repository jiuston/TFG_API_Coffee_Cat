package com.CoffeeCat.service;

import com.CoffeeCat.modelo.usuario.Rol;
import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.repository.UsuarioRepository;
import com.CoffeeCat.utils.ClienteSMPT;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UsuarioService extends BaseService<Usuario,String, UsuarioRepository> {

    private UsuarioRepository usuarioRepository;
    @Lazy
    private PasswordEncoder passwordEncoder;

    public Optional<Usuario> findByEmail(String email){return usuarioRepository.findByEmail(email);}

    //Metodo para registrar usuarios.
    public Usuario createUsuario(Usuario usuario){
        Set<Rol> roles = new HashSet<Rol>();
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        if (usuario.getRoles().size() == 0 || usuario.getRoles() == null) {
            roles.add(Rol.USER);
            usuario.setRoles(roles);
        }
        return usuarioRepository.saveAndFlush(usuario);
    }

    public void modificarPassword(Usuario usuario, String pass, String repitePass) throws Exception {
        if (pass.equals(repitePass)) {
            usuario.setPassword(passwordEncoder.encode(pass));
            usuario.setTokenNuevaPass(null);
            usuarioRepository.saveAndFlush(usuario);
        }else {
            throw new Exception("Las contraseñas no coinciden");
        }
    }

    public String generarToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        usuario.setTokenNuevaPass(token);
        usuarioRepository.saveAndFlush(usuario);
        return token;
    }

    public boolean mandarEmail(String email, String token) {
        ClienteSMPT clienteSMPT = new ClienteSMPT();
        return clienteSMPT.enviarCorreoCambioPass(email,token);
    }
}
