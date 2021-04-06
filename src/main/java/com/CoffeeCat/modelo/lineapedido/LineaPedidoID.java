package com.CoffeeCat.modelo.lineapedido;

import com.CoffeeCat.modelo.pedido.Pedido;
import com.CoffeeCat.modelo.producto.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LineaPedidoID implements Serializable {
    @ManyToOne
    private Pedido pedido;
    private Integer numero_linea;

}
