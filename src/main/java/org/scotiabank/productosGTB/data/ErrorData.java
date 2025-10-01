package org.scotiabank.productosGTB.data;

public class ErrorData {

    public int fila;
    public String columna;
    public String valor;
    public String mensaje;

    public ErrorData(int fila, String columna, String valor, String mensaje) {
        this.fila = fila;
        this.columna = columna;
        this.valor = valor;
        this.mensaje = mensaje;
    }

}
