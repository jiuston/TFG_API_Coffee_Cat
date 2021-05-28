package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.usuario.*;
import com.CoffeeCat.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
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
@Api(tags = "Usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @ApiOperation("Devuelve el usuario actual")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/usuarios/{id_usuario}")
    public ResponseEntity<?> getUsuarioById() {
         String id_usuario=usuarioService.getUserId();
        try {
            Usuario usuario = usuarioService.findById(id_usuario).orElseThrow(() -> new Exception("Usuario con id " + id_usuario + " no existe"));
            return ResponseEntity.status(HttpStatus.OK).body(new UsuarioOutputDTO(usuario));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ApiOperation("Registra un nuevo usuario con rol de USER")
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

    @ApiOperation("Devuelve todos los uuarios de la aplicacion")
    @PreAuthorize("hasRole('ADMIN')")
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

    @ApiOperation("Borra un usuario en concreto")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/usuarios")
    public ResponseEntity<?> deleteUsuarioByEmail(@ApiParam(example = "El email del usuario a borrar") @RequestParam String email) {
        try {
            Usuario usuario = usuarioService.findByEmail(email).orElseThrow(() -> new Exception("Usuario con email " + email + " no existe"));
            usuarioService.delete(usuario);
            return ResponseEntity.status(HttpStatus.OK).body("Se ha borrado al usuario con email " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ApiOperation("Genera un codigo y lo manda por correo al email introducido por el usuario")
    @PostMapping("/usuarios/creartokenpass")
    public ResponseEntity<?> createTokenResetPassword(@ApiParam(example = "El email al que mandar el token") @RequestParam (name = "email") String email){
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

    @ApiOperation("Comprueba el token antes de permitir cambiar la contraseña")
    @PostMapping("/usuarios/resetpassword/{token}")
    public ResponseEntity<?> checkToken(@ApiParam(example = "El token que recibe el usuario al correo") @PathVariable String token, @ApiParam(example = "El email del usuario a cambiar la contraseña") @RequestParam String email){
        try {
            Usuario usuario=usuarioService.findByEmail(email).orElseThrow(() -> new Exception("Usuario con email " + email + " no existe"));
            if (usuario.getTokenNuevaPass().equals(token)) return ResponseEntity.status(HttpStatus.OK).build();
            throw new Exception("El codigo no coincide!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @ApiOperation("Cambia la contraseña del usuario")
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
