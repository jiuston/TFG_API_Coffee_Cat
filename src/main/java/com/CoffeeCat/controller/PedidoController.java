package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.lineapedido.LineaPedido;
import com.CoffeeCat.modelo.lineapedido.LineaPedidoInputDTO;
import com.CoffeeCat.modelo.pedido.Pedido;
import com.CoffeeCat.modelo.pedido.PedidoInputDTO;
import com.CoffeeCat.modelo.pedido.PedidoOutputDTO;
import com.CoffeeCat.modelo.pedido.SimplePedidoOutputDTO;
import com.CoffeeCat.modelo.producto.Producto;
import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.service.LineaPedidoService;
import com.CoffeeCat.service.PedidoService;
import com.CoffeeCat.service.ProductoService;
import com.CoffeeCat.service.UsuarioService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    @ApiOperation("Devuelve todos los pedidos para un dia concreto. Usado por los camareros")
    @GetMapping("/fecha/{fecha}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPedidos(@PathVariable String fecha) {

        List<Pedido> pedidos = pedidoService.findByFecha(fecha);
        List<PedidoOutputDTO> pedidosOutputDTO = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            pedidosOutputDTO.add(new PedidoOutputDTO(pedido));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pedidosOutputDTO);
    }

    @ApiOperation("Devuelve el historico de pedidos de un usuario")
    @GetMapping("/usuario/{id_usuario}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPedidoByIdUsuario(@PathVariable String id_usuario) {
        try {
            usuarioService.findById(id_usuario).orElseThrow(() -> new Exception("Usuario no encontrado"));
            List<Pedido> pedidos =pedidoService.findByUsuarioId(id_usuario);
            List<SimplePedidoOutputDTO> pedidosOutput = new ArrayList<>();
            for (Pedido p : pedidos) {
                pedidosOutput.add(new SimplePedidoOutputDTO(p));
            }
            return ResponseEntity.status(HttpStatus.OK).body(pedidosOutput);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ApiOperation("Devuelve los números de los pedidos que no han sido aun completados")
    @GetMapping("/usuario/{idUsuario}/pendiente")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPedidosPendientesByIdUsuario(@PathVariable String idUsuario){
        try {
            usuarioService.findById(idUsuario).orElseThrow(() -> new Exception("Usuario no encontrado"));
            List<Pedido> pedidosPendientes=pedidoService.findByUsuarioIdAndPending(idUsuario);
            List<SimplePedidoOutputDTO> pedidosOutput = new ArrayList<>();
            for (Pedido p : pedidosPendientes) {
                pedidosOutput.add(new SimplePedidoOutputDTO(p));
            }
            return ResponseEntity.status(HttpStatus.OK).body(pedidosOutput);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ApiOperation("Devuelve un pedido por completo")
    @GetMapping("/{id_pedido}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPedidoById(@PathVariable String id_pedido) {
        try {
            Pedido pedido=pedidoService.findById(id_pedido).orElseThrow(() -> new Exception("Pedido con id " + id_pedido + " no encontrado"));
            PedidoOutputDTO pedidoOutputDTO = new PedidoOutputDTO(pedido);
            return ResponseEntity.status(HttpStatus.OK).body(pedidoOutputDTO.getLineas());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ApiOperation("Añade un pedido nuevo a la base de datos")
    @PostMapping()
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> postPedido(@RequestBody PedidoInputDTO pedidoInputDTO) {
        try {
            String idusuario = pedidoInputDTO.getId_usuario();
            Usuario usuario = usuarioService.findById(idusuario).orElseThrow(() -> new Exception("No existe ese usuario"));
            Pedido pedido = pedidoInputDTO.pedido();
            pedido.setUsuario(usuario);
            pedidoService.save(pedido);
            List<LineaPedidoInputDTO> lineas = pedidoInputDTO.getLineas();
            List<LineaPedido> lineasPedido=new ArrayList<>();
            int numLinea = 1;
            for (LineaPedidoInputDTO linea : lineas) {
                Producto producto = productoService.findById(linea.getId_producto()).orElseThrow(() -> new Exception("No existe el producto " + linea.getId_producto()));
                LineaPedido lineaPedido = new LineaPedido();
                lineaPedido.setProductos(producto);
                lineaPedido.setCantidad(linea.getCantidad());
                lineaPedido.setPrecio_unit(producto.getPrecio());
                lineaPedido.setPedido(pedido);
                lineaPedido.setNumero_linea(numLinea++);
                lineaPedidoService.save(lineaPedido);
                lineasPedido.add(lineaPedido);
            }
            pedido.setLineaPedido(lineasPedido);
            pedidoService.edit(pedido);
            return ResponseEntity.status(HttpStatus.OK).body(new PedidoOutputDTO(pedido));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ApiOperation("Cambia el estado de un pedido a entregado")
    @PutMapping("/{id_pedido}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> cambiarEstadoPedido(@PathVariable String id_pedido) {
        try {
            Pedido pedido = pedidoService.findById(id_pedido).orElseThrow(() -> new Exception("Pedido con id " + id_pedido + " no encontrado"));
            pedido.setEntregado(true);
            pedidoService.save(pedido);
            return ResponseEntity.status(HttpStatus.OK).body(new PedidoOutputDTO(pedido));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ApiOperation("Borra un pedido")
    @DeleteMapping("/{id_pedido}")
    public ResponseEntity<?> borrarPedido(@PathVariable String id_pedido) {
        try {
            Pedido pedido = pedidoService.findById(id_pedido).orElseThrow(() -> new Exception("Pedido con id " + id_pedido + " no encontrado"));
            if (pedido.getEntregado()) throw new Exception("No se puede borrar un pedido que ya se ha entregado.");
            else {
                pedidoService.deleteById(id_pedido);
                return ResponseEntity.status(HttpStatus.OK).body(new PedidoOutputDTO(pedido));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
