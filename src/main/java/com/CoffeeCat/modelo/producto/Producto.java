package com.CoffeeCat.modelo.producto;

import com.CoffeeCat.modelo.familia.Familia;
import com.CoffeeCat.modelo.lineapedido.LineaPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto;
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
