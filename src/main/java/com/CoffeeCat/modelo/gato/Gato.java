package com.CoffeeCat.modelo.gato;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gato")
public class Gato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_gato;
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
