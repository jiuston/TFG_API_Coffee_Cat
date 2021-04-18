package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.familia.Familia;
import com.CoffeeCat.modelo.familia.FamiliaOutputDTO;
import com.CoffeeCat.service.FamiliaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,RequestMethod.DELETE, RequestMethod.PUT })
public class FamiliaController {

    private final String imagenUrl="/familias/imagen/";

    @Autowired
    private FamiliaService familiaService;

    @GetMapping("/familias")
    public ResponseEntity<?> getFamilias(){
        List<Familia> familias=familiaService.findAll();
        List<FamiliaOutputDTO> familiasOutputDTO = new ArrayList<>();
        for (Familia familia : familias) {
            familiasOutputDTO.add(new FamiliaOutputDTO(familia));
        }

        return ResponseEntity.status(HttpStatus.OK).body(familiasOutputDTO);
    }

    @GetMapping("familias/imagen/{id_familia}")
    public ResponseEntity<byte[]> getImagenFamilias(@PathVariable String id_familia){
        try {
            Familia familia=familiaService.findById(id_familia).orElseThrow(() ->new Exception("No se encontró familia con ID "+ id_familia));
            return ResponseEntity.status(HttpStatus.OK).body(familia.getImagen());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/familias")
    public ResponseEntity<?> postFamilia(@RequestParam String nombre, @RequestParam("file") MultipartFile file ) {
        Familia familia = new Familia();
        familia.setNombre(nombre);
        try {
            InputStream  inputStream = file.getInputStream();
            byte[] bytesImagen= new byte[inputStream.read()];
            inputStream.read(bytesImagen);
            familia.setImagen(bytesImagen);
            familiaService.save(familia);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/familias/{id}")
    public ResponseEntity<?> deleteFamilia(@PathVariable String id){
        if (familiaService.findById(id).isPresent()){
            familiaService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró familia con ID "+ id);
        }
    }

}
