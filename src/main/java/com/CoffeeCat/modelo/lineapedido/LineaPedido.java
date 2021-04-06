package com.CoffeeCat.modelo.lineapedido;

import com.CoffeeCat.modelo.producto.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "linea_pedido")
public class LineaPedido {

    @EmbeddedId
    private LineaPedidoID lineaPedidoID;

    @ManyToOne
    private Producto productos;
    private Integer cantidad;
    private Float precio_unit;

}
