package com.CoffeeCat.modelo.lineapedido;

import com.CoffeeCat.configurations.StringPrefixedSequenceIdGenerator;
import com.CoffeeCat.modelo.pedido.Pedido;
import com.CoffeeCat.modelo.producto.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "linea_pedido")
public class LineaPedido {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LineaPed_seq")
    @GenericGenerator(
            name = "LineaPed_seq",
            strategy = "com.CoffeeCat.configurations.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "LIN"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%08d")
            })
    private String id;

    @ManyToOne
    private Pedido pedido;
    private Integer numero_linea;
    @ManyToOne
    private Producto productos;
    private Integer cantidad;
    private Double precioLinea;

}
