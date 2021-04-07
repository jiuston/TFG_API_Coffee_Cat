package com.CoffeeCat.repository;

import com.CoffeeCat.modelo.familia.Familia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamiliaRepository extends JpaRepository<Familia, Integer> {
}
