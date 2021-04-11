package com.CoffeeCat.modelo.pedido;

import com.CoffeeCat.modelo.lineapedido.LineaPedido;
import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.service.UsuarioService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
public class PedidoInputDTO {

    private String fecha_pedido;
    private Float precio;
    private String metodoPago;
    private Boolean entregado;
    private String id_usuario;
    private List<LineaPedido> lineas;

    @Autowired
    private UsuarioService usuarioService;

    public Pedido pedido() throws Exception{
        Pedido pedido = new Pedido();
        pedido.setFecha(this.getFecha_pedido());
        pedido.setPrecio(this.getPrecio());
        pedido.setMetodoPago(MetodoPago.getMetodo(this.getMetodoPago()));
        pedido.setEntregado(false);
        Optional<Usuario> usuarioOPT = usuarioService.findById(this.getId_usuario());
        if (usuarioOPT.isPresent()) {
            pedido.setUsuario(usuarioOPT.get());
        }else {
            throw new Exception("No existe ese usuario");
        }
        pedido.setLineaPedido(lineas);
        return pedido;
    }

}
