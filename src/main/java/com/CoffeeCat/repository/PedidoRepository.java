package com.CoffeeCat.repository;

import com.CoffeeCat.modelo.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, String> {

    List<Pedido> findByFecha(Date fecha);

    List<Pedido> findByUsuarioId(String id_usuario);

    @Query(value = "select P from Pedido P where P.entregado=false and P.usuario = (select U from Usuario U where U.id = :idUsuario)")
    List<Pedido> findByUsuarioIdAndPending(String idUsuario);
}
