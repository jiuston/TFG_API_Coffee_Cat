package com.CoffeeCat.service;

import com.CoffeeCat.modelo.familia.Familia;
import com.CoffeeCat.modelo.producto.Producto;
import com.CoffeeCat.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductoService extends BaseService<Producto, String, ProductoRepository>{


    private ProductoRepository productoRepository;

    public Producto crearProducto(Producto producto, String nombre, String descripcion, Double precio, Boolean activo, MultipartFile file) throws IOException{
        if (nombre!=null) producto.setNombre(nombre);
        if (descripcion!=null) producto.setDescripcion(descripcion);
        if (precio!=null) producto.setPrecio(precio);
        if (activo!=null)producto.setActivo(activo);
        if (file!=null){
             byte[] bytesImagen=file.getBytes();
        producto.setImagen(bytesImagen);}
        return producto;
    }

    public List<Producto> findByFamiliaId(String id) { return productoRepository.findByFamiliaId(id);}
}
