package org.scotiabank.productosGTB.model;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LecturaReporteTEFDetMailCuenta {
    private String tipoDeArchivo;
    private String tipoDeRegistro;
    private String mailBeneficiario;
    private String usoFuturo;
    private String claveRastreo;
    private String referenciaDeAbonoBanxico;
    private String tipoDeOperacion;
    private String filler;
}
