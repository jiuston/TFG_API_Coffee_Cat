package com.CoffeeCat.modelo.familia;

import com.CoffeeCat.modelo.producto.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "familia")
public class Familia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_familia;
    private String nombre;

    @Column(name = "imagen" ,nullable = true)
    @Basic(optional = false, fetch = FetchType.EAGER)
    @Lob()
    private byte[] imagen;
    @OneToMany(mappedBy = "familia")
    private List<Producto> productos;

}
