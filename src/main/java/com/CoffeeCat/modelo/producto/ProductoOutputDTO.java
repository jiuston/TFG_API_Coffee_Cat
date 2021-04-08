package com.CoffeeCat.modelo.producto;

import com.CoffeeCat.modelo.familia.Familia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductoOutputDTO {

    private String id;
    private String nombre;
    private String desripcion;
    private Float precio;
    private byte[] imagen;
    private Boolean activo;

    public ProductoOutputDTO(Producto producto){
        this.setId(producto.getId());
        this.setNombre(producto.getNombre());
        this.setDesripcion(producto.getDescripcion());
        this.setPrecio(producto.getPrecio());
        this.setImagen(producto.getImagen());
        this.setActivo(producto.getActivo());

    }

}
