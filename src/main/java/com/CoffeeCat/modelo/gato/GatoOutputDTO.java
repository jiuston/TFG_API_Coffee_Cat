package com.CoffeeCat.modelo.gato;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class GatoOutputDTO {

    private String nombre;
    private Date fecha_nacimiento;
    private String sexo;
    private String historia;
    private Boolean adoptado;
    private String imagenUrl;


    public GatoOutputDTO(Gato gato){
        this.setNombre(gato.getNombre());
        this.setFecha_nacimiento(gato.getFecha_nacimiento());
        this.setSexo(gato.getSexo().getSexo());
        this.setHistoria(gato.getHistoria());
        this.setAdoptado(gato.getAdoptado());


    }

}
