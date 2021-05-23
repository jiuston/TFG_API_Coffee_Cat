package com.CoffeeCat;

import com.CoffeeCat.modelo.familia.Familia;
import com.CoffeeCat.modelo.producto.Producto;
import com.CoffeeCat.repository.GatoRepository;
import com.CoffeeCat.repository.ProductoRepository;
import com.CoffeeCat.service.BaseService;
import com.CoffeeCat.service.GatoService;
import com.CoffeeCat.service.ProductoService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @InjectMocks
    ProductoService productoService;
    @Mock
    ProductoRepository productoRepository;

    @Test
    public void findByFamiliaIdTest(){
        Producto producto = new Producto();
        producto.setId("PROD0001");
        producto.setNombre("Cafe de prueba");
        producto.setActivo(true);
        Mockito.when(productoRepository.findByFamiliaId("FAM0001")).thenReturn(List.of(producto));

        Producto p = productoService.findByFamiliaId("FAM0001").get(0);
        Assertions.assertEquals("Cafe de prueba",p.getNombre());
    }



}
