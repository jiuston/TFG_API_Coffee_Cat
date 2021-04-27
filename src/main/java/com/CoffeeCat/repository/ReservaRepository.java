package com.CoffeeCat.repository;

import com.CoffeeCat.modelo.reserva.Reserva;
import com.CoffeeCat.modelo.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, String> {

    List<Reserva> findByFecha(Date fecha);
    Optional<Reserva> findByUsuario(Usuario usuario);
}
