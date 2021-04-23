package com.CoffeeCat.service;

import com.CoffeeCat.modelo.gato.Gato;
import com.CoffeeCat.modelo.gato.Sexo;
import com.CoffeeCat.repository.GatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Service
public class GatoService extends BaseService<Gato, String, GatoRepository>{

    public Gato crearGato(Gato gato, String nombre, Date fecha_nacimiento,  String sexo,  String historia, MultipartFile file) throws IOException {
        gato.setNombre(nombre);
        gato.setFecha_nacimiento(fecha_nacimiento);
        gato.setSexo(Sexo.getSexo(sexo));
        gato.setHistoria(historia);
        byte[] bytesImagen= file.getBytes();
        gato.setImagen(bytesImagen);
        return gato;
    }
}
