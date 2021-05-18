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
import java.util.TimeZone;

@Data
@NoArgsConstructor
public class PedidoInputDTO {

    private Double precio;
    private String metodoPago;
    private List<LineaPedidoInputDTO> lineas;

    public Pedido pedido() throws Exception{
        Pedido pedido = new Pedido();
        Date fechaActual=new Date();
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(fechaActual);
        pedido.setFecha(fechaActual);
        pedido.setHora(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        pedido.setMinutos(String.valueOf(calendar.get(Calendar.MINUTE)));
        pedido.setPrecio(this.getPrecio());
        pedido.setMetodoPago(MetodoPago.getMetodo(this.getMetodoPago()));
        pedido.setEntregado(false);
        return pedido;
    }

}
