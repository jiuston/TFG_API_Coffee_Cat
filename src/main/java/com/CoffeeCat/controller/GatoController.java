package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.gato.Gato;
import com.CoffeeCat.modelo.gato.GatoOutputDTO;
import com.CoffeeCat.modelo.gato.Sexo;
import com.CoffeeCat.service.GatoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RequestMapping("/gatos")
public class GatoController {

    private final String imagenUrl = "https://coffee-cat.herokuapp.com/gatos/imagen/";

    @Autowired
    private GatoService gatoService;

    @GetMapping
    public ResponseEntity<?> getGatos() {
        List<Gato> gatos = gatoService.findAll();
        List<GatoOutputDTO> gatosOutputDTO = new ArrayList<>();
        for (Gato gato : gatos) {
            gatosOutputDTO.add(transformarGato(gato));
        }
        return ResponseEntity.status(HttpStatus.OK).body(gatosOutputDTO);
    }

    @GetMapping("/imagen/{id_gato}")
    public ResponseEntity<?> getImagenGato(@PathVariable String id_gato) {
        try {
            Gato gato = gatoService.findById(id_gato).orElseThrow(() -> new Exception("Gato no encontrado con id " + id_gato));
            return ResponseEntity.status(HttpStatus.OK).body(gato.getImagen());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id_gato}")
    public ResponseEntity<?> getGatoById(@PathVariable String id_gato) {
        try {
            Gato gato = gatoService.findById(id_gato).orElseThrow(() -> new Exception("Gato no encontrado con id " + id_gato));
            return ResponseEntity.status(HttpStatus.OK).body(transformarGato(gato));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private GatoOutputDTO transformarGato(Gato gato) {
        GatoOutputDTO gatoOutputDTO = new GatoOutputDTO(gato);
        gatoOutputDTO.setImagenUrl(imagenUrl + gato.getId());
        return gatoOutputDTO;
    }

    @PostMapping("")
    public ResponseEntity<?> postGato(@RequestParam String nombre, Date fecha_nacimiento, @RequestParam String sexo, @RequestParam String historia, @RequestParam MultipartFile file) {
        Gato gato = new Gato();
        gato.setNombre(nombre);
        gato.setSexo(Sexo.getSexo(sexo));
        gato.setHistoria(historia);
        gato.setAdoptado(false);
        gato.setFecha_nacimiento(fecha_nacimiento);
        try {
            InputStream inputStream = file.getInputStream();
            byte[] bytesImagen = new byte[inputStream.read()];
            inputStream.read(bytesImagen);
            gato.setImagen(bytesImagen);
            gatoService.save(gato);
            return ResponseEntity.status(HttpStatus.OK).body(transformarGato(gato));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id_gato}")
    public ResponseEntity<?> modificarGato(@PathVariable String id_gato, @RequestParam String nombre, @RequestParam Date fecha_nacimiento, @RequestParam String sexo, @RequestParam String historia, @RequestParam MultipartFile file) {
        try {
            Gato gato = gatoService.findById(id_gato).orElseThrow(() -> new Exception("Gato no encontrado con id " + id_gato));
            gatoService.crearGato(gato, nombre, fecha_nacimiento, sexo, historia, file);
            gatoService.edit(gato);
            return ResponseEntity.status(HttpStatus.OK).body(new GatoOutputDTO(gato));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id_gato}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable String id_gato, @RequestParam Boolean adoptado, Date fecha_adoptado) {
        try{
           Gato gato = gatoService.findById(id_gato).orElseThrow(() -> new Exception("Gato no encontrado con id " + id_gato));
            gato.setAdoptado(adoptado);
            if (fecha_adoptado != null) {
                gato.setFecha_adoptado(fecha_adoptado);
            }
            gatoService.edit(gato);
            return ResponseEntity.status(HttpStatus.OK).body(transformarGato(gato));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

}

    @DeleteMapping("/{id_gato}")
    public ResponseEntity<?> deleteById(@PathVariable String id_gato) {
        try {
            Gato gato = gatoService.findById(id_gato).orElseThrow(() -> new Exception("Gato no encontrado con id " + id_gato));
            gatoService.deleteById(id_gato);
            return ResponseEntity.status(HttpStatus.OK).body("Borrado el gato " + gato.getNombre());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
