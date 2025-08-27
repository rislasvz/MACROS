package org.scotiabank.productosGTB.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LecturaReporteTEFTrailerBloque {
    private String tipoArchivo;
    private String tipoRegistro;
    private String cantidadMovimientoSolicitadosAltas;
    private String importeMovimientoSolicitadosAltas;
    private String cantidadMovimientoSolicitadosBajas;
    private String importeMovimientoSolicitadosBajas;
    private String cantidadMovimientoAceptadosAltas;
    private String importeMovimientoAceptadosAltas;
    private String cantidadMovimientoAceptadosBajas;
    private String importeMovimientoAceptadosBajas;
    private String cantidadMovimientoRechazadosAltas;
    private String importeMovimientoRechazadosAltas;
    private String cantidadMovimientoRechazadosBajas;
    private String importeMovimientoRechazadosBajas;
    private String cantidadDeMovimeientosPagados;
    private String importeDeMovimeientosPagados;
    private String cantidadDeMovimientosDevueltos;
    private String importeDeMovimientosDevueltos;
    private String cantidadDeMovimientosRech;
    private String importeDeMovimientosRech;
    private String cantidadDeMovimientosBaja;
    private String importeMovimientosBaja;
    private String codigoStausRegistro;
    private String filler;
}
