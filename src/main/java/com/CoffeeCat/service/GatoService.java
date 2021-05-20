package com.CoffeeCat.service;

import com.CoffeeCat.modelo.gato.Gato;
import com.CoffeeCat.modelo.gato.Sexo;
import com.CoffeeCat.repository.GatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class GatoService extends BaseService<Gato, String, GatoRepository> {

    public Gato crearGato(Gato gato, String nombre, String fecha_nacimiento, String sexo, String historia, MultipartFile file) throws IOException {
        if (nombre != null)
            gato.setNombre(nombre);
        if (fecha_nacimiento != null)
            try {
                Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fecha_nacimiento);
                gato.setFecha_nacimiento(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        if (sexo!=null)
        gato.setSexo(Sexo.getSexo(sexo));
        if (historia!=null)
        gato.setHistoria(historia);
        if (file!=null){
            byte[] bytesImagen = file.getBytes();
            gato.setImagen(bytesImagen);
        }
        return gato;
    }
}
