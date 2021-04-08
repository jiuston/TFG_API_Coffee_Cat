package com.CoffeeCat.repository;

import com.CoffeeCat.modelo.gato.Gato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatoRepository extends JpaRepository<Gato, String> {
}
