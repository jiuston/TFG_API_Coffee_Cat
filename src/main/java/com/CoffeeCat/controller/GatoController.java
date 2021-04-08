package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.gato.Gato;
import com.CoffeeCat.modelo.gato.GatoOutputDTO;
import com.CoffeeCat.modelo.gato.Sexo;
import com.CoffeeCat.service.GatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,RequestMethod.DELETE, RequestMethod.PUT })
@RequestMapping("/gatos")
public class GatoController {

    @Autowired
    private GatoService gatoService;

    @GetMapping
    public ResponseEntity<?> getGatos(){
        return ResponseEntity.status(HttpStatus.OK).body(gatoService.findAll());
    }

    @GetMapping("/{id_gato}")
    public ResponseEntity<?> getGatoById(@PathVariable String id_gato){
        Optional<Gato> gatoOPT = gatoService.findById(id_gato);
        if (gatoOPT.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(gatoOPT.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El gato con ID " + id_gato + " no se encontró");
        }
    }


    @PostMapping("")
    public ResponseEntity<?> postGato(@RequestParam String nombre,@RequestParam Date fecha_nacimiento,@RequestParam String sexo,@RequestParam String historia,@RequestParam MultipartFile file){
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
            return ResponseEntity.status(HttpStatus.OK).body(new GatoOutputDTO(gato));
        }catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id_gato}")
    public ResponseEntity<?> modificarGato(@PathVariable String id_gato, @RequestParam String nombre,@RequestParam Date fecha_nacimiento,@RequestParam String sexo,@RequestParam String historia,@RequestParam MultipartFile file){
        Optional<Gato> gatoOPT = gatoService.findById(id_gato);
        if (gatoOPT.isPresent()){
            Gato gato = gatoOPT.get();
            try {
                gatoService.crearGato(gato, nombre, fecha_nacimiento, sexo, historia, file);
                gatoService.edit(gato);
                return ResponseEntity.status(HttpStatus.OK).body(new GatoOutputDTO(gato));
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El gato con ID " + id_gato + " no se encontró");
        }
    }

    @PutMapping("/{id_gato}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable String id_gato, @RequestParam Boolean adoptado, @RequestParam @Nullable Date fecha_adoptado){
        Optional<Gato> gatoOPT = gatoService.findById(id_gato);
        if (gatoOPT.isPresent()){
            Gato gato = gatoOPT.get();
            gato.setAdoptado(adoptado);
            if (fecha_adoptado!=null){
                long milis= fecha_adoptado.getTime()+3600000;
                fecha_adoptado = new Date(milis);
                gato.setFecha_adoptado(fecha_adoptado);
            }

            gatoService.edit(gato);
            return ResponseEntity.status(HttpStatus.OK).body(new GatoOutputDTO(gato));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El gato con ID " + id_gato + " no se encontró");
        }
    }

}
