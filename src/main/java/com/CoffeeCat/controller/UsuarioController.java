package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.usuario.*;
import com.CoffeeCat.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

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

    @GetMapping("/usuarios")
    public ResponseEntity<?> getUsuarios(){
        List<Usuario> usuarios = usuarioService.findAll();
        List<UsuarioOutputDTO> usuarioOutputDTOS= new ArrayList<>();
        for (Usuario u :
                usuarios) {
            usuarioOutputDTOS.add(new UsuarioOutputDTO(u));
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioOutputDTOS);
    }

    @DeleteMapping("/usuarios")
    public ResponseEntity<?> deleteUsuarioByEmail(@RequestParam String email) {
        try {
            Usuario usuario = usuarioService.findByEmail(email).orElseThrow(() -> new Exception("Usuario con email " + email + " no existe"));
            usuarioService.delete(usuario);
            return ResponseEntity.status(HttpStatus.OK).body("Se ha borrado al usuario con email " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/usuarios/creartokenpass")
    public ResponseEntity<?> createTokenResetPassword(@RequestParam (name = "email") String email){
        try {
            Usuario usuario=usuarioService.findByEmail(email).orElseThrow(() -> new Exception("Usuario con email " + email + " no existe"));
            String token=usuarioService.generarToken(usuario);
            if (!usuarioService.mandarEmail(email, token)){
                throw new Exception("No se pudo mandar el email!");
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/usuarios/resetpassword/{token}")
    public ResponseEntity<?> checkToken(@PathVariable String token, @RequestParam String email){
        try {
            Usuario usuario=usuarioService.findByEmail(email).orElseThrow(() -> new Exception("Usuario con email " + email + " no existe"));
            if (usuario.getTokenNuevaPass().equals(token)) return ResponseEntity.status(HttpStatus.OK).build();
            throw new Exception("El codigo no coincide!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/usuarios/resetpassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPass resetPass) {
        try {
            Usuario usuario = usuarioService.findByEmail(resetPass.getEmail()).orElseThrow(() -> new Exception("Usuario con email " + resetPass.getEmail() + " no existe"));
            if (usuario.getTokenNuevaPass().equals(resetPass.getToken())) {
                usuarioService.modificarPassword(usuario, resetPass.getPass(), resetPass.getRepitePass());
                return ResponseEntity.status(HttpStatus.OK).body(new UsuarioOutputDTO(usuario));
            }
            else throw new Exception("El codigo no coincide!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
