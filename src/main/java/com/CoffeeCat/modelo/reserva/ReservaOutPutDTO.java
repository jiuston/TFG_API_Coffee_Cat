package com.CoffeeCat.modelo.reserva;

import com.CoffeeCat.modelo.usuario.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class ReservaOutPutDTO {

    private String id;
    private String fecha;
    private Double hora;
    private Float precio;
    private String id_usuario;

    public ReservaOutPutDTO(Reserva reserva){
        if (reserva==null) return;
        this.setId(reserva.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.setFecha(sdf.format(reserva.getFecha()));
        this.setHora(reserva.getHora());
        this.setPrecio(reserva.getPrecio());
        this.setId_usuario(reserva.getUsuario().getId());

    }

}
