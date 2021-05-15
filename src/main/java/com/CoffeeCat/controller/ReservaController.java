package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.reserva.Reserva;
import com.CoffeeCat.modelo.reserva.ReservaInputDTO;
import com.CoffeeCat.modelo.reserva.ReservaOutPutDTO;
import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.service.ReservaService;
import com.CoffeeCat.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@AllArgsConstructor
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    @GetMapping("")
    public ResponseEntity<?> getReservasByFecha(@RequestParam String fecha) {
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

    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> getReservaByUsuario(@PathVariable String idUsuario) {
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

    @PostMapping("")
    public ResponseEntity<?> postReserva(@RequestBody ReservaInputDTO reservaInputDTO) {
        try {
            Usuario usuario = usuarioService.findById(reservaInputDTO.getId_usuario()).orElseThrow(() -> new Exception("Usuario con id " + reservaInputDTO.getId_usuario() + " no existe"));
            Reserva reserva = reservaService.findByUsuarioId(reservaInputDTO.getId_usuario()).orElse(null);
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

    @DeleteMapping("")
    public ResponseEntity<?> deleteReserva(@RequestParam String idUsuario) {
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
