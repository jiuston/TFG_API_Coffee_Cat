package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.familia.Familia;
import com.CoffeeCat.modelo.producto.Producto;
import com.CoffeeCat.modelo.producto.ProductoOutputDTO;
import com.CoffeeCat.service.FamiliaService;
import com.CoffeeCat.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private FamiliaService familiaService;

    @GetMapping("/familia/{id_familia}")
    public ResponseEntity<?> getProductosPorFamilia(@PathVariable String id_familia) {

        Optional<Familia> familiaOPT = familiaService.findById(id_familia);
        if (familiaOPT.isPresent()) {
            List<Producto> productos = productoService.findByFamilia(familiaOPT.get());
            List<ProductoOutputDTO> productosDTO = new ArrayList<>();
            for (Producto producto : productos) {
                ProductoOutputDTO productoOutputDTO = new ProductoOutputDTO(producto);
                productosDTO.add(productoOutputDTO);
            }
            return ResponseEntity.status(HttpStatus.OK).body(productosDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Familia con id " + id_familia + " no encontrada");
        }
    }

    @GetMapping("/{id_producto}")
    public ResponseEntity<?> getProductoById(@PathVariable String id_producto) {
        Optional<Producto> productoOPT = productoService.findById(id_producto);
        if (productoOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ProductoOutputDTO(productoOPT.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encuentra el producto " + id_producto);
        }
    }

    @PostMapping("/familia/{id_familia}")
    public ResponseEntity<?> postProducto(@PathVariable String id_familia, @RequestParam String nombre, @RequestParam String descripcion, @RequestParam Float precio, @RequestParam Boolean activo, @RequestParam("file") MultipartFile file) {
        Optional<Familia> familiaOPT = familiaService.findById(id_familia);
        if (familiaOPT.isPresent()) {
            Producto producto = new Producto();
            producto.setFamilia(familiaOPT.get());
            try {
              producto = productoService.crearProducto(producto, nombre, descripcion, precio, activo, file);
                productoService.save(producto);
                return ResponseEntity.status(HttpStatus.OK).body(new ProductoOutputDTO(producto));
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Familia con id " + id_familia + " no encontrada");
        }
    }

    /*@DeleteMapping("/{id_producto}")
    public ResponseEntity<?> deleteProducto(@PathVariable String id_producto) {
        Optional<Producto> productoOPT = productoService.findById(id_producto);
        if (productoOPT.isPresent()) {
            productoService.deleteById(id_producto);
            return ResponseEntity.status(HttpStatus.OK).body("Borrado el producto " + id_producto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encuentra el producto " + id_producto);
        }
    }*/

    @PutMapping("/{id_producto}/estado")
    public ResponseEntity<?> cambiarEstadoProducto(@PathVariable String id_producto, @RequestParam Boolean activo){
        Optional<Producto> productoOPT = productoService.findById(id_producto);
        if (productoOPT.isPresent()) {
            Producto producto = productoOPT.get();
            producto.setActivo(activo);
            productoService.edit(producto);
            return ResponseEntity.status(HttpStatus.OK).body(new ProductoOutputDTO(producto));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto con id " + id_producto + " no encontrado");
        }
    }

    @PutMapping("/{id_producto}")
    public ResponseEntity<?> putProducto(@PathVariable String id_producto, @RequestParam String nombre, @RequestParam String descripcion, @RequestParam Float precio, @RequestParam Boolean activo, @RequestParam("file") MultipartFile file) {
        Optional<Producto> productoOPT = productoService.findById(id_producto);
        if (productoOPT.isPresent()) {
            Producto producto = new Producto();
            producto.setId(productoOPT.get().getId());
            producto.setFamilia(productoOPT.get().getFamilia());
            try {
                productoService.crearProducto(producto, nombre, descripcion, precio, activo, file);
                productoService.save(producto);
                return ResponseEntity.status(HttpStatus.OK).body(new ProductoOutputDTO(producto));
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto con id " + id_producto + " no encontrado");
        }
    }


}
