package com.CoffeeCat.modelo.familia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamiliaOutputDTO implements Serializable{
    private Integer id_familia;
    private String nombre;
    private byte[] imagen;

}
