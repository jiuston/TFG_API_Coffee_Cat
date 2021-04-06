package com.CoffeeCat.modelo.usuario;

import com.CoffeeCat.modelo.pedido.Pedido;
import com.CoffeeCat.modelo.reserva.Reserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_usuario;
    private String nombre;
    @Column(unique = true)
    private String email;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Rol> roles;
    private Date fecha_nacimiento;
    private String comentario_admin;

    @OneToMany(mappedBy = "usuario")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "usuario")
    private List<Pedido> pedidos;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set result = new HashSet<>();
        for (Rol rol: roles) {
            SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_" + rol.name());
            result.add(sga);
        }
        return result;
    }

    @Override
    public String getUsername() {
        return getNombre();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
