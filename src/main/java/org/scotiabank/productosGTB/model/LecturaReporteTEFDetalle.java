package org.scotiabank.productosGTB.model;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LecturaReporteTEFDetalle {
    private String tipoArchivo;
    private String tipoRegistro;
    private String tipoMovimiento;
    private String CVEMonedaPago;
    private String importe;
    private String fechaAplicacion;
    private String servicioConcepto;
    private String CVEBeneficiario;
    private String RFCBeneficiario;
    private String nombreBeneficiario;
    private String referenciaDePago;
    private String plazaDePagoSBI;
    private String sucursalDePago;
    private String numeroCuentaBeneficiario;
    private String pais;
    private String ciudadEstado;
    private String tipoCuenta;
    private String digitoIntercambio;
    private String plazaCuentaBanco;
    private String numBancoEmisor;
    private String numBancoReceptor;
    private String diasVigencia;
    private String conceptoPago;
    private String campoUsoEmpresa1;
    private String campoUsoEmpresa2;
    private String campoUsoEmpresa3;
    private String codigoStatusRegistro;
    private String CVECambioInst;
    private String codigoCambioInst;
    private String fechaDePago;
    private String plazaDePago;
    private String sucursalDelPago;
    private String folioUnico;
    private String filler;
}
