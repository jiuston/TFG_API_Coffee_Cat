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
    private Sexo sexo;
    private String historia;
    private Boolean adoptado;
    private byte[] imagen;


    public GatoOutputDTO(Gato gato){
        this.setNombre(gato.getNombre());
        this.setFecha_nacimiento(gato.getFecha_nacimiento());
        this.setSexo(gato.getSexo());
        this.setHistoria(gato.getHistoria());
        this.setAdoptado(gato.getAdoptado());
        this.setImagen(gato.getImagen());

    }

}
