package com.CoffeeCat.modelo.reserva;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class ReservaInputDTO {

    private String fecha;
    private String hora;
    private String minutos;
    private Double precio;


    public Reserva reserva(){
        Reserva reserva=new Reserva();
        try {
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(this.getFecha());
            reserva.setFecha(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reserva.setHora(this.getHora());
        reserva.setMinutos(this.getMinutos());
        reserva.setPrecio(this.getPrecio());
        return reserva;
    }
}
