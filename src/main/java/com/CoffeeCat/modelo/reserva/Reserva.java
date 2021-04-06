package com.CoffeeCat.modelo.reserva;

import com.CoffeeCat.modelo.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_reserva;
    private Date fecha_reserva;
    private Float precio;

    @ManyToOne
    private Usuario usuario;
}
