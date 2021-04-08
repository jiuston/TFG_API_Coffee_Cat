package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.familia.Familia;
import com.CoffeeCat.modelo.producto.Producto;
import com.CoffeeCat.modelo.producto.ProductoInputDTO;
import com.CoffeeCat.modelo.producto.ProductoOutputDTO;
import com.CoffeeCat.service.FamiliaService;
import com.CoffeeCat.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,RequestMethod.DELETE, RequestMethod.PUT })
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private FamiliaService familiaService;

    @GetMapping("/familia/{id_familia}")
    public ResponseEntity<?> getProductosPorFamilia(@PathVariable String id_familia){

        Optional<Familia> familiaOPT = familiaService.findById(id_familia);
        if (familiaOPT.isPresent()){
            List<Producto> productos = productoService.findByFamilia(familiaOPT.get());
            List<ProductoOutputDTO> productosDTO = new ArrayList<>();
            for (Producto producto: productos) {
                ProductoOutputDTO productoOutputDTO = new ProductoOutputDTO(producto);
                productosDTO.add(productoOutputDTO);
            }
            return ResponseEntity.status(HttpStatus.OK).body(productosDTO);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Familia con id " + id_familia + " no encontrada");
        }
    }

    @PostMapping("/familia/{id_familia}")
    public ResponseEntity<?> postProducto(@PathVariable String id_familia, @RequestBody ProductoInputDTO productoInputDTO, @RequestParam("file") MultipartFile file){
        Optional<Familia> familiaOPT = familiaService.findById(id_familia);
        if (familiaOPT.isPresent()){
            Producto producto = productoInputDTO.producto(new Producto());
            producto.setFamilia(familiaOPT.get());
            try {
                InputStream inputStream = file.getInputStream();
                byte[] bytesImagen= new byte[inputStream.read()];
                inputStream.read(bytesImagen);
                producto.setImagen(bytesImagen);
                productoService.save(producto);
                return ResponseEntity.status(HttpStatus.OK).body(producto);
            }catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Familia con id " + id_familia + " no encontrada");
        }
    }



}
