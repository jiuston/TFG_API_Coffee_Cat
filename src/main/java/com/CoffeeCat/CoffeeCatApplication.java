package com.CoffeeCat;

import com.CoffeeCat.modelo.usuario.Rol;
import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

@SpringBootApplication
public class CoffeeCatApplication implements CommandLineRunner {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	@Lazy
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(CoffeeCatApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Setting Spring Boot SetTimeZone
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+1"));
	}

	@Override
	public void run(String... args) throws Exception {
		if (!usuarioService.findByEmail("admin@coffeecat.com").isPresent()){
			Set<Rol> roles= new HashSet<>();
			roles.add(Rol.ADMIN);
			Usuario usuario=new Usuario();
			usuario.setRoles(roles);
			usuario.setEmail("admin@coffeecat.com");
			usuario.setPassword("admin");
			usuario.setNombre("admin");
			usuarioService.createUsuario(usuario);
		}
		if (!usuarioService.findByEmail("user@coffeecat.com").isPresent()){
			Set<Rol> roles= new HashSet<>();
			roles.add(Rol.USER);
			Usuario usuario=new Usuario();
			usuario.setRoles(roles);
			usuario.setEmail("user@coffeecat.com");
			usuario.setPassword("user");
			usuario.setNombre("user");
			usuarioService.createUsuario(usuario);
		}
	}
}
