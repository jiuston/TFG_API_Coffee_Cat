package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.familia.Familia;
import com.CoffeeCat.modelo.familia.FamiliaOutputDTO;
import com.CoffeeCat.service.FamiliaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,RequestMethod.DELETE, RequestMethod.PUT })
@Api(tags = "Familias")
@AllArgsConstructor
public class FamiliaController {

    private final FamiliaService familiaService;

    @ApiOperation("Devuelve todas las familias de productos que hay actualmente")
    @GetMapping("/familias")
    public ResponseEntity<?> getFamilias(){
        List<Familia> familias=familiaService.findAll();
        List<FamiliaOutputDTO> familiasOutputDTO = new ArrayList<>();
        for (Familia familia : familias) {
            FamiliaOutputDTO familiaOutputDTO=new FamiliaOutputDTO(familia);
            familiasOutputDTO.add(familiaOutputDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(familiasOutputDTO);
    }

    @ApiOperation("Devuelve la imagen de la familia que se le pasa por parametro")
    @GetMapping(value="familias/imagen/{id_familia}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImagenFamilias(@ApiParam(example = "FAM00000028" ,value = "ID de la familia de la imagen que quiero") @PathVariable String id_familia){
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            Familia familia=familiaService.findById(id_familia).orElseThrow(Exception::new);
            byte[] imagen=familia.getImagen();
            return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Añade una familia nueva")
    @PostMapping("/familias")
    public ResponseEntity<?> postFamilia(@ApiParam(example = "Granizados" ,value = "El nombre de la nueva familia") @RequestParam String nombre, @ApiParam(value = "Imagen de la nueva familia") @RequestPart("file") MultipartFile file ) {
        Familia familia = new Familia();
        familia.setNombre(nombre);
        try {
            byte [] byteArr=file.getBytes();
            familia.setImagen(byteArr);
            familiaService.save(familia);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Borra la familia que se le pase por parametro si existe")
    @DeleteMapping("/familias/{id}")
    public ResponseEntity<?> deleteFamilia(@ApiParam(example = "FAM00000028" ,value = "ID de la familia a borrar") @PathVariable String id){
        if (familiaService.findById(id).isPresent()){
            familiaService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró familia con ID "+ id);
        }
    }

}
