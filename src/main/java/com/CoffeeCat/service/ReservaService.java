package com.CoffeeCat.service;

import com.CoffeeCat.modelo.reserva.Reserva;
import com.CoffeeCat.repository.ReservaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservaService extends BaseService<Reserva, String, ReservaRepository>{

    private final ReservaRepository reservaRepository;

    public List<Reserva> findByFecha(Date fecha){
        return reservaRepository.findByFecha(fecha);
    }

}
