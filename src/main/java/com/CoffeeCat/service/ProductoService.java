package com.CoffeeCat.service;

import com.CoffeeCat.modelo.familia.Familia;
import com.CoffeeCat.modelo.producto.Producto;
import com.CoffeeCat.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductoService extends BaseService<Producto, String, ProductoRepository>{


    private ProductoRepository productoRepository;

    public List<Producto> findByFamilia(Familia familia) { return productoRepository.findByFamilia(familia);}
}
