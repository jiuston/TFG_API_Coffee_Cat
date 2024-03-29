package com.CoffeeCat.service;

import com.CoffeeCat.modelo.usuario.Rol;
import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.repository.UsuarioRepository;
import com.CoffeeCat.utils.ClienteSMPT;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UsuarioService extends BaseService<Usuario,String, UsuarioRepository> {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    @Lazy
    private final ClienteSMPT clienteSMPT;

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
        String token = UUID.randomUUID().toString().substring(0,8);
        usuario.setTokenNuevaPass(token);
        usuarioRepository.saveAndFlush(usuario);
        return token;
    }

    /**
     * Devuelve el identificador del usuario que hace la peticion
     * @return el ID del usuario, ej: "US00000001"
     */
    public String getUserId(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuario.getId();
    }

    public boolean mandarEmail(String email, String token) {
        return clienteSMPT.enviarCorreoCambioPass(email,token);
    }
}
