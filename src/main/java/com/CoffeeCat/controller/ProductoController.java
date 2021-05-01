package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.familia.Familia;
import com.CoffeeCat.modelo.pedido.PedidoOutputDTO;
import com.CoffeeCat.modelo.producto.Producto;
import com.CoffeeCat.modelo.producto.ProductoOutputDTO;
import com.CoffeeCat.service.FamiliaService;
import com.CoffeeCat.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(readOnly = true)
    public ResponseEntity<?> getProductosPorFamilia(@PathVariable String id_familia) {
        try {
            List<Producto> productos = productoService.findByFamiliaId(id_familia);
            List<ProductoOutputDTO> productosDTO = new ArrayList<>();
            for (Producto producto : productos) {
                productosDTO.add(new ProductoOutputDTO(producto));
            }
            return ResponseEntity.status(HttpStatus.OK).body(productosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping(value = "/familia/imagen/{id_producto}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImagenProducto(@PathVariable String id_producto) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            Producto producto = productoService.findById(id_producto).orElseThrow(() -> new Exception("No se encontró producto con id " + id_producto));
            byte[] imagen = producto.getImagen();
            return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id_producto}")
    public ResponseEntity<?> getProductoById(@PathVariable String id_producto) {
        try {
            Producto producto = productoService.findById(id_producto).orElseThrow(() -> new Exception("No se encontró producto con id " + id_producto));
            return ResponseEntity.status(HttpStatus.OK).body(new ProductoOutputDTO(producto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/familia/{id_familia}")
    public ResponseEntity<?> postProducto(@PathVariable String id_familia, @RequestParam String nombre, @RequestParam String descripcion, @RequestParam Double precio, @RequestParam Boolean activo, @RequestParam("file") MultipartFile file) {
        try {
            Familia familia = familiaService.findById(id_familia).orElseThrow(() -> new Exception("Familia con id " + id_familia + " no encontrada"));
            Producto producto = new Producto();
            producto.setFamilia(familia);
            producto = productoService.crearProducto(producto, nombre, descripcion, precio, activo, file);
            productoService.save(producto);
            return ResponseEntity.status(HttpStatus.OK).body(new ProductoOutputDTO(producto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id_producto}")
    public ResponseEntity<?> deleteProducto(@PathVariable String id_producto) {
        try {
            Producto producto = productoService.findById(id_producto).orElseThrow(() -> new Exception("No se encontró producto con id " + id_producto));
            productoService.delete(producto);
            return ResponseEntity.status(HttpStatus.OK).body("Borrado el producto " + id_producto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id_producto}/estado")
    public ResponseEntity<?> cambiarEstadoProducto(@PathVariable String id_producto, @RequestParam Boolean activo) {
        try {
            Producto producto = productoService.findById(id_producto).orElseThrow(() -> new Exception("No se encontró producto con id " + id_producto));
            producto.setActivo(activo);
            return ResponseEntity.status(HttpStatus.OK).body(new ProductoOutputDTO(producto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PutMapping("/{id_producto}")
    public ResponseEntity<?> putProducto(@PathVariable String id_producto, @RequestParam String nombre, @RequestParam String descripcion, @RequestParam Double precio, @RequestParam Boolean activo, @RequestParam("file") MultipartFile file) {
        try {
            Producto producto = productoService.findById(id_producto).orElseThrow(() -> new Exception("No se encontró producto con id " + id_producto));
            productoService.crearProducto(producto, nombre, descripcion, precio, activo, file);
            productoService.save(producto);
            return ResponseEntity.status(HttpStatus.OK).body(new ProductoOutputDTO(producto));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
