package com.CoffeeCat.modelo.lineapedido;

import com.CoffeeCat.modelo.producto.Producto;
import com.CoffeeCat.modelo.producto.ProductoOutputDTO;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class LineaPedidoOutputDTO {
    private Integer numero_linea;
    private ProductoOutputDTO producto;
    private Integer cantidad;

    public LineaPedidoOutputDTO(LineaPedido l) {
        this.numero_linea=l.getNumero_linea();
        this.producto=new ProductoOutputDTO(l.getProductos());
        this.cantidad=l.getCantidad();
    }
}
