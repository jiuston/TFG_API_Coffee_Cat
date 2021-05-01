package com.CoffeeCat.service;

import com.CoffeeCat.modelo.gato.Gato;
import com.CoffeeCat.modelo.gato.Sexo;
import com.CoffeeCat.repository.GatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class GatoService extends BaseService<Gato, String, GatoRepository>{

    public Gato crearGato(Gato gato, String nombre, String fecha_nacimiento,  String sexo,  String historia, MultipartFile file) throws IOException {
        gato.setNombre(nombre);
        try {
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fecha_nacimiento);
            gato.setFecha_nacimiento(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        gato.setSexo(Sexo.getSexo(sexo));
        gato.setHistoria(historia);
        byte[] bytesImagen= file.getBytes();
        gato.setImagen(bytesImagen);
        return gato;
    }
}
