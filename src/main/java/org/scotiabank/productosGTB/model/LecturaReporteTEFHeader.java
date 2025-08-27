package org.scotiabank.productosGTB.model;


import lombok.*;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class LecturaReporteTEFHeader {
    private String tipoDeArchivo;
    private String tipoDeRegistro;
    private String numeroDeConvenio;
    private String secuencia;
    private String fechaGeneracionArchivo;
    private String fechaInicial;
    private String fechaFinal;
    private String codigoStatusRegistro;
    private String filler;
}



