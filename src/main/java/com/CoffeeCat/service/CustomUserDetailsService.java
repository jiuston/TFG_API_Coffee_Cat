package com.CoffeeCat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No encontrado el email "+ email));
    }

    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        return usuarioService.findById(id).orElseThrow(() -> new UsernameNotFoundException("No encontrado el id "+ id));
    }
}
