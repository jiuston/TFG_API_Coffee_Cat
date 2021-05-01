package com.CoffeeCat.repository;

import com.CoffeeCat.modelo.familia.Familia;
import com.CoffeeCat.modelo.producto.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {

    List<Producto> findByFamiliaId(String id);



}
