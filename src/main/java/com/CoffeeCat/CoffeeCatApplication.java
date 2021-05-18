package com.CoffeeCat;

import com.CoffeeCat.modelo.usuario.Rol;
import com.CoffeeCat.modelo.usuario.Usuario;
import com.CoffeeCat.service.UsuarioService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CoffeeCatApplication implements CommandLineRunner {

	private final UsuarioService usuarioService;

	public static void main(String[] args) {
		SpringApplication.run(CoffeeCatApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {
		if (!usuarioService.findByEmail("usuarioprimero@coffeecat.com").isPresent()){
			Set<Rol> roles= new HashSet<>();
			roles.add(Rol.ADMIN);
			Usuario usuario=new Usuario();
			usuario.setRoles(roles);
			usuario.setEmail("usuarioprimero@coffeecat.com");
			usuario.setPassword("usuarioprimero");
			usuario.setNombre("usuarioprimero");
			usuarioService.createUsuario(usuario);
		}
		if (!usuarioService.findByEmail("usuariosegundo@coffeecat.com").isPresent()){
			Set<Rol> roles= new HashSet<>();
			roles.add(Rol.USER);
			Usuario usuario=new Usuario();
			usuario.setRoles(roles);
			usuario.setEmail("usuariosegundo@coffeecat.com");
			usuario.setPassword("usuariosegundo");
			usuario.setNombre("usuariosegundo");
			usuarioService.createUsuario(usuario);
		}
	}
}
