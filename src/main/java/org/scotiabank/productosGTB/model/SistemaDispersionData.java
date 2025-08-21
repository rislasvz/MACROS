package org.scotiabank.productosGTB.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SistemaDispersionData {

    private StringProperty formaPago;
    private StringProperty tipoCuenta;
    private StringProperty bancoReceptor;
    private StringProperty cuenta;
    private StringProperty importePago;
    private StringProperty claveBeneficiario;
    private StringProperty rfcBeneficiario;
    private StringProperty nombreBeneficiario;
    private StringProperty referenciaPago;
    private StringProperty conceptoPago;
    private StringProperty diasVigencia;
    private StringProperty infoAgruparPagos;
    private StringProperty detalleMail;
    private StringProperty referenciaAbonoBanxico;
    private StringProperty tipoOperación;

    public SistemaDispersionData(String formaPago, String tipoCuenta, String bancoReceptor, String cuenta, String importePago, String claveBeneficiario, String rfcBeneficiario, String nombreBeneficiario, String referenciaPago, String conceptoPago, String diasVigencia, String infoAgruparPagos, String detalleMail, String referenciaAbonoBanxico, String tipoOperación) {
        this.formaPago = new SimpleStringProperty(formaPago);
        this.tipoCuenta = new SimpleStringProperty(tipoCuenta);
        this.bancoReceptor = new SimpleStringProperty(bancoReceptor);
        this.cuenta = new SimpleStringProperty(cuenta);
        this.importePago = new SimpleStringProperty(importePago);
        this.claveBeneficiario = new SimpleStringProperty(claveBeneficiario);
        this.rfcBeneficiario = new SimpleStringProperty(rfcBeneficiario);
        this.nombreBeneficiario = new SimpleStringProperty(nombreBeneficiario);
        this.referenciaPago = new SimpleStringProperty(referenciaPago);
        this.conceptoPago = new SimpleStringProperty(conceptoPago);
        this.diasVigencia = new SimpleStringProperty(diasVigencia);
        this.infoAgruparPagos = new SimpleStringProperty(infoAgruparPagos);
        this.detalleMail = new SimpleStringProperty(detalleMail);
        this.referenciaAbonoBanxico = new SimpleStringProperty(referenciaAbonoBanxico);
        this.tipoOperación = new SimpleStringProperty(tipoOperación);
    }

    public SistemaDispersionData() {
        this.formaPago = new SimpleStringProperty("");
        this.tipoCuenta = new SimpleStringProperty("");
        this.bancoReceptor = new SimpleStringProperty("");
        this.cuenta = new SimpleStringProperty("");
        this.importePago = new SimpleStringProperty("");
        this.claveBeneficiario = new SimpleStringProperty("");
        this.rfcBeneficiario = new SimpleStringProperty("");
        this.nombreBeneficiario = new SimpleStringProperty("");
        this.referenciaPago = new SimpleStringProperty("");
        this.conceptoPago = new SimpleStringProperty("");
        this.diasVigencia = new SimpleStringProperty("");
        this.infoAgruparPagos = new SimpleStringProperty("");
        this.detalleMail = new SimpleStringProperty("");
        this.referenciaAbonoBanxico = new SimpleStringProperty("");
        this.tipoOperación = new SimpleStringProperty("");
    }

    public String getFormaPago() {
        return formaPago.get();
    }

    public StringProperty formaPagoProperty() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago.set(formaPago);
    }

    public String getTipoCuenta() {
        return tipoCuenta.get();
    }

    public StringProperty tipoCuentaProperty() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta.set(tipoCuenta);
    }

    public String getBancoReceptor() {
        return bancoReceptor.get();
    }

    public StringProperty bancoReceptorProperty() {
        return bancoReceptor;
    }

    public void setBancoReceptor(String bancoReceptor) {
        this.bancoReceptor.set(bancoReceptor);
    }

    public String getCuenta() {
        return cuenta.get();
    }

    public StringProperty cuentaProperty() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta.set(cuenta);
    }

    public String getImportePago() {
        return importePago.get();
    }

    public StringProperty importePagoProperty() {
        return importePago;
    }

    public void setImportePago(String importePago) {
        this.importePago.set(importePago);
    }

    public String getClaveBeneficiario() {
        return claveBeneficiario.get();
    }

    public StringProperty claveBeneficiarioProperty() {
        return claveBeneficiario;
    }

    public void setClaveBeneficiario(String claveBeneficiario) {
        this.claveBeneficiario.set(claveBeneficiario);
    }

    public String getNombreBeneficiario() {
        return nombreBeneficiario.get();
    }

    public StringProperty nombreBeneficiarioProperty() {
        return nombreBeneficiario;
    }

    public void setNombreBeneficiario(String nombreBeneficiario) {
        this.nombreBeneficiario.set(nombreBeneficiario);
    }

    public String getRfcBeneficiario() {
        return rfcBeneficiario.get();
    }

    public StringProperty rfcBeneficiarioProperty() {
        return rfcBeneficiario;
    }

    public void setRfcBeneficiario(String rfcBeneficiario) {
        this.rfcBeneficiario.set(rfcBeneficiario);
    }

    public String getReferenciaPago() {
        return referenciaPago.get();
    }

    public StringProperty referenciaPagoProperty() {
        return referenciaPago;
    }

    public void setReferenciaPago(String referenciaPago) {
        this.referenciaPago.set(referenciaPago);
    }

    public String getConceptoPago() {
        return conceptoPago.get();
    }

    public StringProperty conceptoPagoProperty() {
        return conceptoPago;
    }

    public void setConceptoPago(String conceptoPago) {
        this.conceptoPago.set(conceptoPago);
    }

    public String getDiasVigencia() {
        return diasVigencia.get();
    }

    public StringProperty diasVigenciaProperty() {
        return diasVigencia;
    }

    public void setDiasVigencia(String diasVigencia) {
        this.diasVigencia.set(diasVigencia);
    }

    public String getInfoAgruparPagos() {
        return infoAgruparPagos.get();
    }

    public StringProperty infoAgruparPagosProperty() {
        return infoAgruparPagos;
    }

    public void setInfoAgruparPagos(String infoAgruparPagos) {
        this.infoAgruparPagos.set(infoAgruparPagos);
    }

    public String getReferenciaAbonoBanxico() {
        return referenciaAbonoBanxico.get();
    }

    public StringProperty referenciaAbonoBanxicoProperty() {
        return referenciaAbonoBanxico;
    }

    public void setReferenciaAbonoBanxico(String referenciaAbonoBanxico) {
        this.referenciaAbonoBanxico.set(referenciaAbonoBanxico);
    }

    public String getDetalleMail() {
        return detalleMail.get();
    }

    public StringProperty detalleMailProperty() {
        return detalleMail;
    }

    public void setDetalleMail(String detalleMail) {
        this.detalleMail.set(detalleMail);
    }

    public String getTipoOperación() {
        return tipoOperación.get();
    }

    public StringProperty tipoOperaciónProperty() {
        return tipoOperación;
    }

    public void setTipoOperación(String tipoOperación) {
        this.tipoOperación.set(tipoOperación);
    }
}
