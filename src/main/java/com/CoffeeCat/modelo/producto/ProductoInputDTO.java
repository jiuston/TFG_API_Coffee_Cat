package com.CoffeeCat.modelo.producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Properties;

@Data
@NoArgsConstructor
public class ProductoInputDTO {
    private String nombre;
    private String descripcion;
    private Float precio;
    private Boolean activo;


    public Producto producto(Producto producto){
        if (producto ==null) return null;
        if (this.getNombre() != null) producto.setNombre(this.getNombre());
        if (this.getDescripcion() != null) producto.setDescripcion(this.getDescripcion());
        if (this.getPrecio() != null) producto.setPrecio(this.getPrecio());
        if (this.getActivo() != null) producto.setActivo(this.getActivo());
        return producto;
    }

}
