package com.CoffeeCat.modelo.pedido;

import com.CoffeeCat.configurations.StringPrefixedSequenceIdGenerator;
import com.CoffeeCat.modelo.lineapedido.LineaPedido;
import com.CoffeeCat.modelo.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Pedido_seq")
    @GenericGenerator(
            name = "Pedido_seq",
            strategy = "com.CoffeeCat.configurations.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "PED"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%08d")
            })
    private String id;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private String hora;
    private String minutos;
    private Double precio;
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    private Boolean entregado;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<LineaPedido> lineaPedido;

}
