package com.CoffeeCat.modelo.pedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class PedidoOutputDTO {

    private String id;
    private String fecha_pedido;
    private Float precio;
    private String metodoPago;
    private String id_usuario;

    public PedidoOutputDTO(Pedido pedido){
        if (pedido==null) return;
        this.setId(pedido.getId());
        this.setFecha_pedido(pedido.getFecha());
        this.setPrecio(pedido.getPrecio());
        this.setMetodoPago(pedido.getMetodoPago().getMetodo());
    }

}
