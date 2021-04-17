package com.CoffeeCat.repository;

import com.CoffeeCat.modelo.lineapedido.LineaPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineaPedidoRepository extends JpaRepository<LineaPedido, String> {
}
