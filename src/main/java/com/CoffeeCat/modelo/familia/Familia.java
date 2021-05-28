package com.CoffeeCat.modelo.familia;

import com.CoffeeCat.configurations.StringPrefixedSequenceIdGenerator;
import com.CoffeeCat.modelo.producto.Producto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "familia")
public class Familia {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "familia_seq")
    @GenericGenerator(
            name = "familia_seq",
            strategy = "com.CoffeeCat.configurations.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "FAM"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%08d")
            })
    private String id;
    private String nombre;

    @Column(name = "imagen" , length = 4096000)
    @Lob()
    private byte[] imagen;
    @OneToMany(mappedBy = "familia", cascade = CascadeType.ALL)
    private List<Producto> productos;

    @Override
    public String toString() {
        return "Familia{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", productos=" + productos +
                '}';
    }
}
