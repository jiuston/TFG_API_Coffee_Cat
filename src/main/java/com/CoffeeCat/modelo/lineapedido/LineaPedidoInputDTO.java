package com.CoffeeCat.modelo.lineapedido;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LineaPedidoInputDTO {

    private Integer numeroLinea;
    private String id_producto;
    private Integer cantidad;
}
