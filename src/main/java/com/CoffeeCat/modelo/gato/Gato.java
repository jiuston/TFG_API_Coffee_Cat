package com.CoffeeCat.modelo.gato;

import com.CoffeeCat.configurations.StringPrefixedSequenceIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gato")
public class Gato {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gato_seq")
    @GenericGenerator(
            name = "gato_seq",
            strategy = "com.CoffeeCat.configurations.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "GAT"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%08d")
            })
    private String id;
    private String nombre;
    private Date fecha_nacimiento;

    @Enumerated(value = EnumType.STRING)
    private Sexo sexo;
    private String historia;
    private Boolean adoptado;
    private Date fecha_adoptado;

    @Column(name = "imagen" ,nullable = true)
    @Basic(optional = false, fetch = FetchType.EAGER)
    @Lob()
    private byte[] imagen;
}
