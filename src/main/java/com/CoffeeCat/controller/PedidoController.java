package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.lineapedido.LineaPedido;
import com.CoffeeCat.modelo.lineapedido.LineaPedidoInputDTO;
import com.CoffeeCat.modelo.pedido.Pedido;
import com.CoffeeCat.modelo.pedido.PedidoInputDTO;
import com.CoffeeCat.modelo.pedido.PedidoOutputDTO;
import com.CoffeeCat.modelo.producto.Producto;
import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.service.LineaPedidoService;
import com.CoffeeCat.service.PedidoService;
import com.CoffeeCat.service.ProductoService;
import com.CoffeeCat.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private LineaPedidoService lineaPedidoService;

    @GetMapping("/{fecha}")
    public ResponseEntity<?> getPedidos(@PathVariable String fecha) {

        List<Pedido> pedidos = pedidoService.findByFecha(fecha);
        List<PedidoOutputDTO> pedidosOutputDTO = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            pedidosOutputDTO.add(new PedidoOutputDTO(pedido));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pedidosOutputDTO);
    }

    @GetMapping("/{id_pedido}/usuario/{id_usuario}")
    public ResponseEntity<?> getPedidoById(@PathVariable String id_pedido, @PathVariable String id_usuario) {
        Optional<Usuario> usuarioOPT = usuarioService.findById(id_usuario);
        if (!usuarioOPT.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se ha encontrado un usario con esa ID");
        Optional<Pedido> pedidoOPT = pedidoService.findById(id_pedido);
        if (pedidoOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(new PedidoOutputDTO(pedidoOPT.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se ha encontrado un pedido con ese número.");
        }
    }

    @PostMapping()
    public ResponseEntity<?> postPedido(@RequestBody PedidoInputDTO pedidoInputDTO) {
        try {
            String idusuario = pedidoInputDTO.getId_usuario();
            Usuario usuario = usuarioService.findById(idusuario).orElseThrow(() -> new Exception("No existe ese usuario"));
            Pedido pedido = pedidoInputDTO.pedido();
            pedido.setUsuario(usuario);
            pedidoService.save(pedido);
            List<LineaPedidoInputDTO> lineas = pedidoInputDTO.getLineas();
            int numLinea=1;
            for (LineaPedidoInputDTO linea : lineas) {
                Producto producto = productoService.findById(linea.getId_producto()).orElseThrow(() -> new Exception("No existe el producto " + linea.getId_producto()));
                LineaPedido lineaPedido = new LineaPedido();
                lineaPedido.setProductos(producto);
                lineaPedido.setCantidad(linea.getCantidad());
                lineaPedido.setPrecio_unit(producto.getPrecio());
                lineaPedido.setPedido(pedido);
                lineaPedido.setNumero_linea(numLinea++);
                lineaPedidoService.save(lineaPedido);
            }

            return ResponseEntity.status(HttpStatus.OK).body(new PedidoOutputDTO(pedido));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id_pedido")
    public ResponseEntity<?> cambiarEstadoPedido(@PathVariable String id_pedido) {
        Optional<Pedido> pedidoOPT = pedidoService.findById(id_pedido);
        if (pedidoOPT.isPresent()) {
            Pedido pedido = pedidoOPT.get();
            pedido.setEntregado(true);
            pedidoService.save(pedido);
            return ResponseEntity.status(HttpStatus.OK).body(new PedidoOutputDTO(pedido));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un pedido con ese número");
        }
    }

    @DeleteMapping("/{id_pedido")
    public ResponseEntity<?> borrarPedido(@PathVariable String id_pedido) {
        Optional<Pedido> pedidoOPT = pedidoService.findById(id_pedido);
        if (pedidoOPT.isPresent()) {
            if (pedidoOPT.get().getEntregado()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede borrar un pedido que ya se ha entregado.");
            } else {
                pedidoService.deleteById(id_pedido);
                return ResponseEntity.status(HttpStatus.OK).body("Borrado el pedido " + id_pedido);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un pedido con ese número");
        }
    }

}
