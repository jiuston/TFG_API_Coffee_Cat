package com.CoffeeCat.modelo.pedido;

import com.CoffeeCat.modelo.lineapedido.LineaPedido;
import com.CoffeeCat.modelo.lineapedido.LineaPedidoInputDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PedidoInputDTO {

    private String fecha_pedido;
    private Double precio;
    private String hora;
    private String minutos;
    private String metodoPago;
    private Boolean entregado;
    private String id_usuario;
    private List<LineaPedidoInputDTO> lineas;

    public Pedido pedido() throws Exception{
        Pedido pedido = new Pedido();
        try {
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(this.getFecha_pedido());
            pedido.setFecha(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pedido.setHora(this.getHora());
        pedido.setMinutos(this.getMinutos());
        pedido.setPrecio(this.getPrecio());
        pedido.setMetodoPago(MetodoPago.getMetodo(this.getMetodoPago()));
        pedido.setEntregado(false);
        return pedido;
    }

}
