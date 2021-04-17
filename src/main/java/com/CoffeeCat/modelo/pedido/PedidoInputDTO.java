package com.CoffeeCat.modelo.pedido;

import com.CoffeeCat.modelo.lineapedido.LineaPedidoInputDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PedidoInputDTO {

    private String fecha_pedido;
    private Float precio;
    private String metodoPago;
    private Boolean entregado;
    private String id_usuario;
    private List<LineaPedidoInputDTO> lineas;

    public Pedido pedido() throws Exception{
        Pedido pedido = new Pedido();
        pedido.setFecha(this.getFecha_pedido());
        pedido.setPrecio(this.getPrecio());
        pedido.setMetodoPago(MetodoPago.getMetodo(this.getMetodoPago()));
        pedido.setEntregado(false);
        //pedido.setLineaPedido(lineas);
        return pedido;
    }

}
