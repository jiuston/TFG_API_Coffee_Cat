package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.gato.Gato;
import com.CoffeeCat.modelo.gato.GatoOutputDTO;
import com.CoffeeCat.service.GatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RequestMapping("/gatos")
public class GatoController {


    @Autowired
    private GatoService gatoService;

    @GetMapping
    public ResponseEntity<?> getGatos() {
        List<Gato> gatos = gatoService.findAll();
        List<GatoOutputDTO> gatosOutputDTO = new ArrayList<>();
        for (Gato gato : gatos) {
            gatosOutputDTO.add(new GatoOutputDTO(gato));
        }
        return ResponseEntity.status(HttpStatus.OK).body(gatosOutputDTO);
    }

    @GetMapping(value = "/imagen/{id_gato}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImagenGato(@PathVariable String id_gato) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            Gato gato = gatoService.findById(id_gato).orElseThrow(() -> new Exception("Gato no encontrado con id " + id_gato));
            byte[] imagen = gato.getImagen();
            return new ResponseEntity<>(imagen,headers,HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id_gato}")
    public ResponseEntity<?> getGatoById(@PathVariable String id_gato) {
        try {
            Gato gato = gatoService.findById(id_gato).orElseThrow(() -> new Exception("Gato no encontrado con id " + id_gato));
            return ResponseEntity.status(HttpStatus.OK).body(new GatoOutputDTO(gato));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> postGato(@RequestParam String nombre, Date fecha_nacimiento, @RequestParam String sexo, @RequestParam String historia, @RequestParam MultipartFile file) {
        Gato gato = new Gato();
        gato.setAdoptado(false);
        try {
            gatoService.crearGato(gato, nombre, fecha_nacimiento, sexo, historia, file);
            gatoService.save(gato);
            return ResponseEntity.status(HttpStatus.OK).body(new GatoOutputDTO(gato));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id_gato}")
    public ResponseEntity<?> modificarGato(@PathVariable String id_gato, String nombre,  Date fecha_nacimiento,  String sexo,  String historia,  MultipartFile file) {
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
            return ResponseEntity.status(HttpStatus.OK).body(new GatoOutputDTO(gato));
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
