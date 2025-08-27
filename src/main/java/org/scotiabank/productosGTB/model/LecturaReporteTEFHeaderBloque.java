package org.scotiabank.productosGTB.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LecturaReporteTEFHeaderBloque {
    private String tipoDeArchivo;
    private String tipoDeRegistro;
    private String monedaCTARegistro;
    private String usoFuturo;
    private String cuentaDeCargo;
    private String referecniaEmpresa;
    private String codigoStatusRegistro;
    private String filler;
}
