package com.CoffeeCat.controller;

import com.CoffeeCat.modelo.gato.Gato;
import com.CoffeeCat.modelo.gato.GatoOutputDTO;
import com.CoffeeCat.service.GatoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(tags = "Gatos")
@AllArgsConstructor
@RequestMapping("/gatos")
public class GatoController {

    private final GatoService gatoService;

    @ApiOperation("Devuelve todos los gatos que hay actualmente en el local")
    @GetMapping
    public ResponseEntity<?> getGatos() {
        List<Gato> gatos = gatoService.findAll();
        List<GatoOutputDTO> gatosOutputDTO = new ArrayList<>();
        for (Gato gato : gatos) {
            gatosOutputDTO.add(new GatoOutputDTO(gato));
        }
        return ResponseEntity.status(HttpStatus.OK).body(gatosOutputDTO);
    }

    @ApiOperation("Devuelve la imagen que corresponda al gato que se requiere por variable")
    @GetMapping(value = "/imagen/{id_gato}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImagenGato(@ApiParam(example = "GAT00000023" ,value = "ID del gato del que quiero la imagen") @PathVariable String id_gato) {
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

    @ApiOperation("Devuelve el gato que se requiere por su ID")
    @GetMapping("/{id_gato}")
    public ResponseEntity<?> getGatoById(@ApiParam(example = "GAT00000023" ,value = "ID del gato") @PathVariable String id_gato) {
        try {
            Gato gato = gatoService.findById(id_gato).orElseThrow(() -> new Exception("Gato no encontrado con id " + id_gato));
            return ResponseEntity.status(HttpStatus.OK).body(new GatoOutputDTO(gato));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    @ApiOperation("Añade un gato nuevo a la base de datos en función de los parametros que se le pasen. La fecha de nacimiento no es obligatoria")
    public ResponseEntity<?> postGato(@RequestParam String nombre, String fecha_nacimiento, @RequestParam String sexo, @RequestParam String historia, @RequestPart MultipartFile file) {
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

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Modifica la informacion de un gato")
    @PutMapping("/{id_gato}")
    public ResponseEntity<?> modificarGato(@PathVariable String id_gato, String nombre,  String fecha_nacimiento,  String sexo,  String historia, @RequestPart @Nullable MultipartFile file) {
        try {
            Gato gato = gatoService.findById(id_gato).orElseThrow(() -> new Exception("Gato no encontrado con id " + id_gato));
            gatoService.crearGato(gato, nombre, fecha_nacimiento, sexo, historia, file);
            gatoService.edit(gato);
            return ResponseEntity.status(HttpStatus.OK).body(new GatoOutputDTO(gato));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Modifica el estado de un gato a \"Adoptado\"")
    @PutMapping("/{id_gato}/estado")
    public ResponseEntity<?> actualizarEstado(@ApiParam(example = "GAT00000023" ,value = "ID del gato a modificar") @PathVariable String id_gato, @ApiParam(value = "Estado del gato") @RequestParam Boolean adoptado, @ApiParam(value = "28/05/2021",example = "Fecha de adopcion") Date fecha_adoptado) {
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

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Elimina el gato correspondiente de la base de datos")
    @DeleteMapping("/{id_gato}")
    public ResponseEntity<?> deleteById(@ApiParam(value = "GAT00000023", example = "Id del gato que quiero borrar") @PathVariable String id_gato) {
        try {
            Gato gato = gatoService.findById(id_gato).orElseThrow(() -> new Exception("Gato no encontrado con id " + id_gato));
            gatoService.deleteById(id_gato);
            return ResponseEntity.status(HttpStatus.OK).body("Borrado el gato " + gato.getNombre());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
