package com.CoffeeCat.modelo.gato;

public enum Sexo {
    MACHO("Macho"),
    HEMBRA("Hembra");

    private String sexo;

    Sexo(String sexo) {
        this.sexo = sexo;
    }

    public String getSexo() {
        return sexo;
    }

    public static Sexo getSexo(String sexo){
        if (sexo.equals("Macho")) return Sexo.MACHO;
        if (sexo.equals("Hembra")) return Sexo.HEMBRA;
        return null;
    }

}
