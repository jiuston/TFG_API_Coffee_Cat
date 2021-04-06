package com.CoffeeCat.modelo.pedido;

import com.CoffeeCat.modelo.lineapedido.LineaPedido;
import com.CoffeeCat.modelo.lineapedido.LineaPedidoID;
import com.CoffeeCat.modelo.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pedido;
    private Date fecha_pedido;
    private Float precio;
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    private Boolean entregado;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "lineaPedidoID.pedido")
    private List<LineaPedido> lineaPedido;

}
