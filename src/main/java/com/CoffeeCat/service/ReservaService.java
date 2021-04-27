package com.CoffeeCat.service;

import com.CoffeeCat.modelo.reserva.Reserva;
import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.repository.ReservaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservaService extends BaseService<Reserva, String, ReservaRepository>{

    private final ReservaRepository reservaRepository;

    public Optional<Reserva> findByUsuario(Usuario usuario){ return reservaRepository.findByUsuario(usuario);}

    public List<Reserva> findByFecha(Date fecha){
        return reservaRepository.findByFecha(fecha);
    }

}
