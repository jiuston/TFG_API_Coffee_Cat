package com.CoffeeCat.modelo.producto;

import com.CoffeeCat.configurations.StringPrefixedSequenceIdGenerator;
import com.CoffeeCat.modelo.familia.Familia;
import com.CoffeeCat.modelo.lineapedido.LineaPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Producto_seq")
    @GenericGenerator(
            name = "Producto_seq",
            strategy = "com.CoffeeCat.configurations.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "PROD"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%08d")
            })
    private String id;
    private String nombre;
    private String descripcion;
    private Float precio;

    @Column(name = "imagen" ,nullable = true)
    @Basic(optional = false, fetch = FetchType.EAGER)
    @Lob()
    private byte[] imagen;
    private Boolean activo;

    @ManyToOne
    private Familia familia;

    @OneToMany(mappedBy = "productos")
    private List<LineaPedido> lineaPedido;

}
