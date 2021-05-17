package com.CoffeeCat.modelo.pedido;

import com.CoffeeCat.modelo.lineapedido.LineaPedidoInputDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PedidoInputDTO {

    private Double precio;
    private String metodoPago;
    private List<LineaPedidoInputDTO> lineas;

    public Pedido pedido() throws Exception{
        Pedido pedido = new Pedido();
        Calendar calendar= Calendar.getInstance();
       pedido.setFecha(new Date());
        pedido.setHora(String.valueOf(calendar.get(Calendar.HOUR)));
        pedido.setMinutos(String.valueOf(calendar.get(Calendar.MINUTE)));
        pedido.setPrecio(this.getPrecio());
        pedido.setMetodoPago(MetodoPago.getMetodo(this.getMetodoPago()));
        pedido.setEntregado(false);
        return pedido;
    }

}
