package org.scotiabank.productosGTB.model;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class LecturaTEFXLSTOXLSX {
    private int numero;
    private String formaPago;
    private String tipoCuenta;
    private String bancoReceptor;
    private String cuenta;
    private String importePago;
    private String claveBeneficiario;
    private String rfcBeneficiario;
    private String nombreBeneficiario;
    private String referenciaPago;
    private String conceptoPago;
    private String diasVigencia;
    private String campoDeInformacionParaAgruparPagos;
    private String detalleMail;
    private String getReferenciaAbonoBanxico;
    private String tipoDeOperacion;
}
