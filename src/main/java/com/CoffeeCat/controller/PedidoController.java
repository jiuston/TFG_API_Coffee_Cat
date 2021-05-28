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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@AllArgsConstructor
@Api(tags = "Pedidos")
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final LineaPedidoService lineaPedidoService;

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Devuelve todos los pedidos para un dia concreto. Usado por los camareros")
    @GetMapping("/fecha")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPedidos(@ApiParam(value = "28/05/2021",example = "Fecha para buscar los pedidos") @RequestParam String fecha) {
       try{
           Date fechaBuscada = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
           List<Pedido> pedidos = pedidoService.findByFecha(fechaBuscada);
           List<PedidoOutputDTO> pedidosOutputDTO = new ArrayList<>();
           for (Pedido pedido : pedidos) {
               pedidosOutputDTO.add(new PedidoOutputDTO(pedido));
           }
           return ResponseEntity.status(HttpStatus.OK).body(pedidosOutputDTO);
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
       }


    }

    @ApiOperation("Devuelve el historico de pedidos de un usuario")
    @GetMapping("/usuario/historial")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPedidoByIdUsuario() {
        String idUsuario= usuarioService.getUserId();
        try {
            usuarioService.findById(idUsuario).orElseThrow(() -> new Exception("Usuario no encontrado"));
            List<Pedido> pedidos =pedidoService.findByUsuarioId(idUsuario);
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
    @GetMapping("/usuario/pendiente")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPedidosPendientesByIdUsuario(){
        String idUsuario= usuarioService.getUserId();
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
    public ResponseEntity<?> getPedidoById(@ApiParam(value = "PED00000049",example = "Numero del pedido a buscar") @PathVariable String id_pedido) {
        try {
            Pedido pedido=pedidoService.findById(id_pedido).orElseThrow(() -> new Exception("Pedido con id " + id_pedido + " no encontrado"));

            return ResponseEntity.status(HttpStatus.OK).body(new PedidoOutputDTO(pedido));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ApiOperation("Añade un pedido nuevo a la base de datos")
    @PostMapping()
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> postPedido(@RequestBody PedidoInputDTO pedidoInputDTO) {
        String IdUsuario= usuarioService.getUserId();
        try {
            Usuario usuario = usuarioService.findById(IdUsuario).orElseThrow(() -> new Exception("No existe ese usuario"));
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
                lineaPedido.setPrecioLinea(producto.getPrecio()*linea.getCantidad());
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
    public ResponseEntity<?> cambiarEstadoPedido(@ApiParam(value = "PED00000049",example = "Numero del pedido a completar") @PathVariable String id_pedido) {
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
    public ResponseEntity<?> borrarPedido(@ApiParam(value = "PED00000049",example = "Numero del pedido a borrar") @PathVariable String id_pedido) {
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
