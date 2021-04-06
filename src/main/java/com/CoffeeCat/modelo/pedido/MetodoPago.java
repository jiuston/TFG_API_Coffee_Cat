package com.CoffeeCat.modelo.pedido;

public enum MetodoPago {

    EFECTIVO("Efectivo"), TARJETA("Tarjeta");

    private String metodo;

    MetodoPago(String metodo) {
        this.metodo = metodo;
    }

    public String getMetodo() {
        return metodo;
    }

    public static MetodoPago getMetodo(String metodo){
        if (metodo.equals("Efectivo")) return MetodoPago.EFECTIVO;
        if (metodo.equals("Tarjeta")) return MetodoPago.TARJETA;
        return null;
    }

}
