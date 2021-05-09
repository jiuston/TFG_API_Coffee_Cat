package com.CoffeeCat.modelo.pedido;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimplePedidoOutputDTO {
    private String id;

    public SimplePedidoOutputDTO(Pedido pedido) {
        this.id = pedido.getId();
    }
}
