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

    public Producto crearProducto(Producto producto, String nombre, String descripcion, Float precio, Boolean activo, MultipartFile file) throws IOException{
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setActivo(activo);
        byte[] bytesImagen=file.getBytes();
        producto.setImagen(bytesImagen);
        return producto;
    }

    public List<Producto> findByFamilia(Familia familia) { return productoRepository.findByFamilia(familia);}
}
