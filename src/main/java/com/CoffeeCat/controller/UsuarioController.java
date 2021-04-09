package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.usuario.*;
import com.CoffeeCat.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,RequestMethod.DELETE, RequestMethod.PUT })
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/usuarios/{id_usuario}")
    public ResponseEntity<?> getUsuarioById(@PathVariable String id_usuario){
        Optional<Usuario> usuarioOPT = usuarioService.findById(id_usuario);
        if (usuarioOPT.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(new UsuarioOutputDTO(usuarioOPT.get()));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario con id " + id_usuario + " no existe");
        }
    }

    /*@GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioInputLoginDTO usuarioInput){
        Usuario usuarioOPT = usuarioService.findByEmail(usuarioInput.getEmail());
    }*/

    @PostMapping("/register")
    public ResponseEntity<?> registro(@RequestBody UsuarioRegistroInputDTO usuario){
        Optional<Usuario> usuarioOPT = usuarioService.findByEmail(usuario.getEmail());
        if (usuarioOPT.isPresent()) return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un usuario con este email");
        Usuario nuevoUsuario = usuario.usuario();
        Set<Rol> roles = new HashSet<>();
        roles.add(Rol.USER);
        nuevoUsuario.setRoles(roles);
        usuarioService.save(nuevoUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(new UsuarioOutputDTO(nuevoUsuario));
    }


    @DeleteMapping("usuario/{id_usuario}")
    public ResponseEntity<?> deleteUsuario(@PathVariable String id_usuario){
        Optional<Usuario> usuarioOPT = usuarioService.findById(id_usuario);
        if (usuarioOPT.isPresent()){
            usuarioService.deleteById(id_usuario);
            return ResponseEntity.status(HttpStatus.OK).body("Se ha borrado al usuario con id " + id_usuario);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se ha encontrado al usuario con id " + id_usuario);
        }
    }

}
