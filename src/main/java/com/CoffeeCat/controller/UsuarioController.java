package com.CoffeeCat.controller;

import com.CoffeeCat.configurations.security.ClaseValidacion;
import com.CoffeeCat.modelo.usuario.*;
import com.CoffeeCat.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClaseValidacion claseValidacion;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/usuarios/{id_usuario}")
    public ResponseEntity<?> getUsuarioById(@PathVariable String id_usuario) {
        try {
            Usuario usuario = usuarioService.findById(id_usuario).orElseThrow(() -> new Exception("Usuario con id " + id_usuario + " no existe"));
            return ResponseEntity.status(HttpStatus.OK).body(new UsuarioOutputDTO(usuario));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /*@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioInputLoginDTO usuarioInput){



        //TODO   Todo fue bien
        String uuid= UUID.randomUUID().toString();
        claseValidacion.getValidacion().put(usuarioInput.getEmail(), uuid);
        return ResponseEntity.status(HttpStatus.OK).body(uuid);
    }*/

    @PostMapping("/register")
    public ResponseEntity<?> registro(@RequestBody UsuarioRegistroInputDTO usuarioRegistro) {
        try {
            if(usuarioService.findByEmail(usuarioRegistro.getEmail()).isPresent()) throw new Exception("Un usuario con este email ya existe");
            Usuario nuevoUsuario = usuarioRegistro.usuario();
            nuevoUsuario= usuarioService.createUsuario(nuevoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioOutputDTO(nuevoUsuario));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }


    @DeleteMapping("usuario/")
    public ResponseEntity<?> deleteUsuarioByEmail(@RequestParam String email) {
        try {
            Usuario usuario = usuarioService.findByEmail(email).orElseThrow(() -> new Exception("Usuario con id " + email + " no existe"));
            usuarioService.delete(usuario);
            return ResponseEntity.status(HttpStatus.OK).body("Se ha borrado al usuario con email " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
