package com.CoffeeCat.modelo.familia;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FamiliaDTOConverter {

    @Autowired
    private final ModelMapper modelMapper;

    public FamiliaDTOConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public FamiliaOutputDTO convert(Familia familia){
        return modelMapper.map(familia, FamiliaOutputDTO.class);
    }

}
