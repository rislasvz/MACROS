package org.scotiabank.productosGTB.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;

@Data
public class PreRegistroCuentasData {

    private IntegerProperty num;
    private StringProperty tipoRegistro;
    private StringProperty tipoPago;
    private StringProperty tipoCuenta;
    private StringProperty monedaCuenta;
    private StringProperty claveBanco;
    private StringProperty cuentaAbono;
    private StringProperty limiteTransaccion;
    private StringProperty fechaEliminacion;
    private StringProperty descripcion;

    public PreRegistroCuentasData(Integer num, String tipoRegistro, String tipoPago, String tipoCuenta, String monedaCuenta, String claveBanco, String cuentaAbono, String limiteTransaccion, String fechaEliminacion, String descripcion) {
        this.num = new SimpleIntegerProperty(num);
        this.tipoRegistro = new SimpleStringProperty(tipoRegistro);
        this.tipoPago = new SimpleStringProperty(tipoPago);
        this.tipoCuenta = new SimpleStringProperty(tipoCuenta);
        this.monedaCuenta = new SimpleStringProperty(monedaCuenta);
        this.claveBanco = new SimpleStringProperty(claveBanco);
        this.cuentaAbono = new SimpleStringProperty(cuentaAbono);
        this.limiteTransaccion = new SimpleStringProperty(limiteTransaccion);
        this.fechaEliminacion = new SimpleStringProperty(fechaEliminacion);
        this.descripcion = new SimpleStringProperty(descripcion);
    }

    public PreRegistroCuentasData() {
        this.num = new SimpleIntegerProperty();
        this.tipoRegistro = new SimpleStringProperty("");
        this.tipoPago = new SimpleStringProperty("");
        this.tipoCuenta = new SimpleStringProperty("");
        this.monedaCuenta = new SimpleStringProperty("");
        this.claveBanco = new SimpleStringProperty("");
        this.cuentaAbono = new SimpleStringProperty("");
        this.limiteTransaccion = new SimpleStringProperty("");
        this.fechaEliminacion = new SimpleStringProperty("");
        this.descripcion = new SimpleStringProperty("");
    }

    public int getNum() {
        return num.get();
    }

    public IntegerProperty numProperty() {
        return num;
    }

    public void setNum(int num) {
        this.num.set(num);
    }

    public String getTipoRegistro() {
        return tipoRegistro.get();
    }

    public StringProperty tipoRegistroProperty() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro.set(tipoRegistro);
    }

    public String getTipoPago() {
        return tipoPago.get();
    }

    public StringProperty tipoPagoProperty() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago.set(tipoPago);
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

    public String getMonedaCuenta() {
        return monedaCuenta.get();
    }

    public StringProperty monedaCuentaProperty() {
        return monedaCuenta;
    }

    public void setMonedaCuenta(String monedaCuenta) {
        this.monedaCuenta.set(monedaCuenta);
    }

    public String getClaveBanco() {
        return claveBanco.get();
    }

    public StringProperty claveBancoProperty() {
        return claveBanco;
    }

    public void setClaveBanco(String claveBanco) {
        this.claveBanco.set(claveBanco);
    }

    public String getCuentaAbono() {
        return cuentaAbono.get();
    }

    public StringProperty cuentaAbonoProperty() {
        return cuentaAbono;
    }

    public void setCuentaAbono(String cuentaAbono) {
        this.cuentaAbono.set(cuentaAbono);
    }

    public String getLimiteTransaccion() {
        return limiteTransaccion.get();
    }

    public StringProperty limiteTransaccionProperty() {
        return limiteTransaccion;
    }

    public void setLimiteTransaccion(String limiteTransaccion) {
        this.limiteTransaccion.set(limiteTransaccion);
    }

    public String getFechaEliminacion() {
        return fechaEliminacion.get();
    }

    public StringProperty fechaEliminacionProperty() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(String fechaEliminacion) {
        this.fechaEliminacion.set(fechaEliminacion);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }


}
