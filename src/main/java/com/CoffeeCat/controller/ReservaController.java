package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.reserva.Reserva;
import com.CoffeeCat.modelo.reserva.ReservaInputDTO;
import com.CoffeeCat.modelo.reserva.ReservaOutPutDTO;
import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.service.ReservaService;
import com.CoffeeCat.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(tags = "Reservas")
@AllArgsConstructor
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    @ApiOperation("Devuelve todas las reservas para una fecha dada")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getReservasByFecha(@ApiParam(value = "28/05/2021",example = "Fecha de la que queremos saber las reservas") @RequestParam String fecha) {
        try {
            Date fechaBuscada = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
            List<Reserva> reservas = reservaService.findByFecha(fechaBuscada);
            List<ReservaOutPutDTO> reservasDTO = new ArrayList<>();
            for (Reserva reserva : reservas) {
                reservasDTO.add(new ReservaOutPutDTO(reserva));
            }
            return ResponseEntity.status(HttpStatus.OK).body(reservasDTO);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ha habido un error al buscar por la fecha " + fecha);
        }
    }

    @ApiOperation("Devuelve las reservas de un usuario")
    @GetMapping("/usuario")
    public ResponseEntity<?> getReservaByUsuario() {
            String idUsuario = usuarioService.getUserId();
        try {
            Reserva reserva = reservaService.findByUsuarioId(idUsuario).orElse(null);
            if (reserva!=null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ReservaOutPutDTO(reserva));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ApiOperation("Hace una reserva para la fecha y hora especificadas")
    @PostMapping("")
    public ResponseEntity<?> postReserva(@RequestBody ReservaInputDTO reservaInputDTO) {
        String idUsuario = usuarioService.getUserId();
        try {
            Usuario usuario = usuarioService.findById(idUsuario).orElseThrow(() -> new Exception("Usuario con id " + idUsuario + " no existe"));
            Reserva reserva = reservaService.findByUsuarioId(idUsuario).orElse(null);
            if (reserva != null) throw new Exception("Ya tienes una reserva");
            Date fechaBuscada = new SimpleDateFormat("dd/MM/yyyy").parse(reservaInputDTO.getFecha());
            List<Reserva> reservas = reservaService.findByFecha(fechaBuscada);
            for (Reserva r : reservas) {
                if (r.getHora().equals(reservaInputDTO.getHora())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Esa hora ya está reservada");
                }
            }
            reserva = reservaInputDTO.reserva();

            reserva.setUsuario(usuario);
            reservaService.save(reserva);
            return ResponseEntity.status(HttpStatus.OK).body(new ReservaOutPutDTO(reserva));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @ApiOperation("Borra la reserva del usuario que hace la peticion")
    @DeleteMapping("")
    public ResponseEntity<?> deleteReserva() {
        String idUsuario = usuarioService.getUserId();
        try {
            Usuario usuario = usuarioService.findById(idUsuario).orElseThrow(() -> new Exception("Usuario con id " + idUsuario + " no encontrado"));
            Reserva reserva = reservaService.findByUsuarioId(idUsuario).orElseThrow(() -> new Exception("Este usuario no tiene reservas"));
            reservaService.delete(reserva);
            return ResponseEntity.status(HttpStatus.OK).body("Borrada la reserva de " + usuario.getNombre());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

}
