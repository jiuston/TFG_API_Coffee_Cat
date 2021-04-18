package com.CoffeeCat.modelo.familia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FamiliaOutputDTO implements Serializable{
    private String id;
    private String nombre;
    private String imagenUrl;
    private byte[] imagen;

    public FamiliaOutputDTO(Familia familia){
        this.setId(familia.getId());
        this.setNombre(familia.getNombre());
    }

}
