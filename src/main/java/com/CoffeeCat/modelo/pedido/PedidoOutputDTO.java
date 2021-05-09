package com.CoffeeCat.modelo.pedido;

import com.CoffeeCat.modelo.lineapedido.LineaPedido;
import com.CoffeeCat.modelo.lineapedido.LineaPedidoOutputDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PedidoOutputDTO {

    private String id;
    private String fecha_pedido;
    private String hora;
    private String minutos;
    private Double precio;
    private String metodoPago;
    private String id_usuario;
    private List<LineaPedidoOutputDTO> lineas;

    public PedidoOutputDTO(Pedido pedido){
        if (pedido==null) return;
        this.setId(pedido.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.setFecha_pedido(sdf.format(pedido.getFecha()));
        this.setHora(pedido.getHora());
        this.setMinutos(pedido.getMinutos());
        this.setPrecio(pedido.getPrecio());
        this.setMetodoPago(pedido.getMetodoPago().getMetodo());
        this.setId_usuario(pedido.getUsuario().getId());
        List<LineaPedidoOutputDTO> lineas= new ArrayList<>();
        for (LineaPedido l : pedido.getLineaPedido()) {
            lineas.add(new LineaPedidoOutputDTO(l));
        }
        this.setLineas(lineas);
    }

}
