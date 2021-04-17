package com.CoffeeCat.service;

import com.CoffeeCat.modelo.lineapedido.LineaPedido;
import com.CoffeeCat.repository.LineaPedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LineaPedidoService extends BaseService<LineaPedido, String, LineaPedidoRepository>{
}
