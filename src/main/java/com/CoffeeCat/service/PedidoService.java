package com.CoffeeCat.service;

import com.CoffeeCat.modelo.pedido.Pedido;
import com.CoffeeCat.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class PedidoService extends BaseService<Pedido, String, PedidoRepository> {

    private final PedidoRepository pedidoRepository;

    public List<Pedido> findByFecha(String fecha){
        return pedidoRepository.findByFecha(fecha);
    }

    public List<Pedido> findByUsuarioId(String id_usuario) {
        return pedidoRepository.findByUsuarioId(id_usuario);

    }

    public List<Pedido> findByUsuarioIdAndPending(String idUsuario) {
        List<Pedido> pedidos = findByUsuarioId(idUsuario);
        List<Pedido> pedidosPendientes = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (!p.getEntregado()) pedidosPendientes.add(p);
        }
        return pedidosPendientes;
    }
}
