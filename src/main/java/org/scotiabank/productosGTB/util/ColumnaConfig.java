package org.scotiabank.productosGTB.util;

import java.util.List;
import java.util.function.BiFunction;

public class ColumnaConfig {

    public String nombre;
    public int indice;
    public BiFunction<String, List<String>, Boolean> validador; // Función que recibe el valor y una lista (si aplica)
    public String mensajeError;
    public List<String> valoresPermitidos; // Usado solo para isValidAllowedValue

    public ColumnaConfig(String nombre, int indice, BiFunction<String, List<String>, Boolean> validador, String mensajeError, List<String> valoresPermitidos) {
        this.nombre = nombre;
        this.indice = indice;
        this.validador = validador;
        this.mensajeError = mensajeError;
        this.valoresPermitidos = valoresPermitidos;
    }
}
