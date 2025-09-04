package org.scotiabank.productosGTB.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.scotiabank.productosGTB.data.TooltipMessages;
import org.scotiabank.productosGTB.enums.FileTypesEnum;
import org.scotiabank.productosGTB.enums.PaymentConceptEnum;
import org.scotiabank.productosGTB.enums.PaymentCurrencyEnum;
import org.scotiabank.productosGTB.model.SistemaDispersionData;
import org.scotiabank.productosGTB.util.Constants;
import org.scotiabank.productosGTB.util.TooltipManager;
import org.scotiabank.productosGTB.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SistemaDispercionFondosController {

    private static final Logger log = LoggerFactory.getLogger(SistemaDispercionFondosController.class);
    @FXML
    private TextField textFieldClientNumber;
    @FXML
    private TextField textFieldFileNumberOfTheDay;
    @FXML
    private TextField textFieldChargeAccount;
    @FXML
    private TextField textFieldCompanyReference;
    @FXML
    private ComboBox<FileTypesEnum> comboBoxFileType;
    @FXML
    private ComboBox<PaymentConceptEnum> comboBoxPaymentConcept;
    @FXML
    private ComboBox<PaymentCurrencyEnum> comboBoxPaymentCurrency;
    @FXML
    private DatePicker datePicketFechaAplicacion;
    @FXML
    private TableView<SistemaDispersionData> tableViewDispersionFondos;
    private ObservableList<SistemaDispersionData> dataList;
    @FXML
    private TableColumn<SistemaDispersionData, Integer> num;
    @FXML
    private TableColumn<SistemaDispersionData, String> formaPago;
    @FXML
    private TableColumn<SistemaDispersionData, String> tipoCuenta;
    @FXML
    private TableColumn<SistemaDispersionData, String> cuenta;
    @FXML
    private TableColumn<SistemaDispersionData, String> importePago;
    @FXML
    private TableColumn<SistemaDispersionData, String> claveBeneficiario;
    @FXML
    private TableColumn<SistemaDispersionData, String> rfcBeneficiario;
    @FXML
    private TableColumn<SistemaDispersionData, String> nombreBeneficiario;
    @FXML
    private TableColumn<SistemaDispersionData, String> referenciaPago;
    @FXML
    private TableColumn<SistemaDispersionData, String> conceptoPago;
    @FXML
    private TableColumn<SistemaDispersionData, String> diasVigencia;
    @FXML
    private TableColumn<SistemaDispersionData, String> infoAgruparPagos;
    @FXML
    private TableColumn<SistemaDispersionData, String> detalleMail;
    @FXML
    private TableColumn<SistemaDispersionData, String> referenciaAbonoBanxico;
    @FXML
    private TableColumn<SistemaDispersionData, String> tipoOperacion;
    @FXML
    private TableColumn<SistemaDispersionData, String> bancoReceptor;
    @FXML
    private Label errorLabelClientNumber;
    @FXML
    private Label errorLabelChargeAccount;
    @FXML
    private Label errorLabelFileNumberOfTheDay;
    @FXML
    private Label errorLabelCompanyReference;

    @FXML
    public void initialize() {
        Util.validaNumeros(textFieldClientNumber);
        Util.validaNumeros(textFieldFileNumberOfTheDay);
        Util.validaNumeros(textFieldCompanyReference);
        Util.validaNumeros(textFieldChargeAccount);
        fillAllComboBox();
        agregaTooltips();

        dataList = FXCollections.observableArrayList(
                new SistemaDispersionData(1, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
        );
        tableViewDispersionFondos.setItems(dataList);
        fillTable();
    }

    public void fillAllComboBox(){
        comboBoxFileType.setItems(FXCollections.observableArrayList(FileTypesEnum.values()));
        comboBoxFileType.getSelectionModel().selectFirst();
        comboBoxPaymentConcept.setItems(FXCollections.observableArrayList(PaymentConceptEnum.values()));
        comboBoxPaymentConcept.getSelectionModel().selectFirst();
        comboBoxPaymentCurrency.setItems(FXCollections.observableArrayList(PaymentCurrencyEnum.values()));
        comboBoxPaymentCurrency.getSelectionModel().selectFirst();
    }

    public void fillTable(){
        formaPago.setCellValueFactory(new PropertyValueFactory<>("formaPago"));
        tipoCuenta.setCellValueFactory(new PropertyValueFactory<>("tipoCuenta"));
        bancoReceptor.setCellValueFactory(new PropertyValueFactory<>("bancoReceptor"));
        cuenta.setCellValueFactory(new PropertyValueFactory<>("cuenta"));
        importePago.setCellValueFactory(new PropertyValueFactory<>("importePago"));
        claveBeneficiario.setCellValueFactory(new PropertyValueFactory<>("claveBeneficiario"));
        rfcBeneficiario.setCellValueFactory(new PropertyValueFactory<>("rfcBeneficiario"));
        nombreBeneficiario.setCellValueFactory(new PropertyValueFactory<>("nombreBeneficiario"));
        referenciaPago.setCellValueFactory(new PropertyValueFactory<>("referenciaPago"));
        conceptoPago.setCellValueFactory(new PropertyValueFactory<>("conceptoPago"));
        diasVigencia.setCellValueFactory(new PropertyValueFactory<>("diasVigencia"));
        infoAgruparPagos.setCellValueFactory(new PropertyValueFactory<>("infoAgruparPagos"));
        detalleMail.setCellValueFactory(new PropertyValueFactory<>("detalleMail"));
        referenciaAbonoBanxico.setCellValueFactory(new PropertyValueFactory<>("referenciaAbonoBanxico"));
        tipoOperacion.setCellValueFactory(new PropertyValueFactory<>("tipoOperacion"));

        num.setCellValueFactory(new PropertyValueFactory<>("num"));
        formaPago.setCellFactory(Util.createNumericCellFactory(2, 2));
        tipoCuenta.setCellFactory(Util.createNumericCellFactory(1, 1));
        bancoReceptor.setCellFactory(Util.createNumericCellFactory(3, 3));
        cuenta.setCellFactory(Util.createNumericCellFactory(11, 20));
        importePago.setCellFactory(Util.createDecimalCellFactory(3, 15));
        claveBeneficiario.setCellFactory(Util.createStringWithoutSymbolsCellFactory(1, 20));
        rfcBeneficiario.setCellFactory(Util.createStringWithoutSymbolsCellFactory(12, 13));
        nombreBeneficiario.setCellFactory(Util.createStringWithoutSymbolsCellFactory(1, 40));
        referenciaPago.setCellFactory(Util.createNumericCellFactory(1, 16));
        conceptoPago.setCellFactory(Util.createStringWithoutSymbolsCellFactory(1, 40));
        diasVigencia.setCellFactory(Util.createNumericCellFactory(1, 2));
        infoAgruparPagos.setCellFactory(Util.createStringWithoutSymbolsCellFactory(1, 60));
        detalleMail.setCellFactory(Util.createStringEmailCellFactory(1, 60));
        referenciaAbonoBanxico.setCellFactory(Util.createNumericCellFactory(1, 20));
        tipoOperacion.setCellFactory(Util.createNumericCellFactory(2, 2));
        tableViewDispersionFondos.setEditable(true);
    }

    @FXML
    private void agregarFila(ActionEvent event) {
        Integer nuevoNumero = dataList.size() + 1;
        SistemaDispersionData nuevaFila = new SistemaDispersionData(
                nuevoNumero, "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
        );
        dataList.add(nuevaFila);
    }

    @FXML
    private void eliminarFila(ActionEvent event) {
        // Solo elimina la fila si hay más de una
        if (dataList.size() > 1) {
            dataList.remove(dataList.size() - 1);
        } else {
            // Opcional: mostrar una alerta o mensaje al usuario
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error de Eliminación");
            alert.setHeaderText(null);
            alert.setContentText("No puedes eliminar la última fila de la tabla.");
            alert.showAndWait();
        }
    }

    public void agregaTooltips(){
        TooltipManager.applyTooltip(textFieldClientNumber, TooltipMessages.CLIENT_NUMBER_TOOLTIP);
        TooltipManager.applyTooltip(textFieldFileNumberOfTheDay, TooltipMessages.FILE_NUMBRER_OF_THE_DAY_TOOLTIP);
        TooltipManager.applyTooltip(textFieldChargeAccount, TooltipMessages.CHARGE_ACCOUNT_TOOLTIP);
        TooltipManager.applyTooltip(textFieldCompanyReference, TooltipMessages.COMPANY_REFERENCE_TOOLTIP);
        //TooltipManager.applyTooltip(textFieldFileType, TooltipMessages.FILE_TYPE_TOOLTIP);
        //TooltipManager.applyTooltip(comboBoxPaymentConcept, TooltipMessages.PAYMENT_CONCEPT_TOOLTIP);
        //TooltipManager.applyTooltip(comboBoxPaymentCurrency, TooltipMessages.PAYMENT_CURRENCY_TOOLTIP);
    }

    public void exportarDatosAArchivoOptimizada() {
        tableViewDispersionFondos.refresh();
        if (validateForm()) {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyyMMdd");

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Archivo de Datos");

            // Configurar la extensión del archivo
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            // Mostrar el cuadro de diálogo para guardar el archivo
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                try (
                        FileWriter fw = new FileWriter(file);
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter writer = new PrintWriter(bw)
                ) {
                    Integer contadorAltasOBajas = 0;
                    Double contadorImportes = 0.0;
                    //Escribimos las dos primeras lineas del txt
                    writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                            + Constants.TIPO_REGISTRO_HA
                            + Util.rellenarConCerosIzquierda(textFieldClientNumber.getText(), 5)
                            + Util.rellenarConCerosIzquierda(textFieldFileNumberOfTheDay.getText(), 2)
                            +"000000000000000000000000000");
                    writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                            + Constants.TIPO_REGISTRO_HB
                            + Util.rellenarConCerosIzquierda(comboBoxPaymentCurrency.toString(), 2)
                            + "0000" + Util.rellenarConCerosIzquierda(textFieldChargeAccount.getText(), 11)
                            + Util.rellenarConCerosIzquierda(textFieldCompanyReference.getText(), 10) + "000");

                    for (SistemaDispersionData data : dataList) {
                        //Sumamos uno al contador de registors que se han hecho
                        contadorAltasOBajas++;
                        contadorImportes = Double.valueOf(contadorImportes + data.getImportePago());
                        //PRIMERA LINEA QUE SE REPITE POR CADA REGISTRO DE LA TABLA
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                                + Constants.TIPO_REGISTRO_DA
                                + data.getFormaPago()
                                + Util.rellenarConCerosIzquierda(comboBoxPaymentCurrency.getValue().getId(), 2)
                                + Util.rellenarConCerosIzquierda(data.getImportePago().replaceAll("\\.", ""), 15)
                                + datePicketFechaAplicacion.getValue().format(formato)
                                + Util.rellenarConCerosIzquierda(comboBoxPaymentConcept.getValue().getId(), 2)
                                + Util.rellenarConEspaciosDerecha(data.getClaveBeneficiario(), 20)
                                + data.getRfcBeneficiario()
                                + Util.rellenarConEspaciosDerecha(data.getNombreBeneficiario(), 40)
                                + Util.rellenarConCerosIzquierda(data.getReferenciaPago(), 16)
                                + Util.rellenarConCerosIzquierda("", 10)
                                + Util.rellenarConCerosIzquierda(data.getCuenta(), 20)
                                + Util.rellenarConCerosIzquierda("", 5)
                                + Util.rellenarConEspaciosDerecha("", 40)
                                + data.getTipoCuenta()
                                + Util.rellenarConEspaciosDerecha("", 1)
                                + Util.rellenarConCerosIzquierda("", 5)
                                + Constants.CLAVE_BANCO_EMISOR
                                + Util.rellenarConCerosIzquierda(data.getBancoReceptor(), 5)
                                + Util.rellenarConCerosIzquierda(data.getDiasVigencia(), 3)
                                + Util.rellenarConEspaciosDerecha(data.getConceptoPago(), 50)
                                + Util.rellenarConEspaciosDerecha(data.getInfoAgruparPagos(), 60)
                                + Util.rellenarConCerosIzquierda("", 25)
                                + Util.rellenarConEspaciosDerecha(data.getInfoAgruparPagos(), 16));
                        //SEGUNDA LINEA DEL TXT
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA + Constants.TIPO_REGISTRO_DM + data.getDetalleMail());
                    }
                    if(Objects.equals(comboBoxFileType.getValue().getId(), "1")){
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA + Constants.TIPO_REGISTRO_TB
                                        + Util.rellenarConCerosIzquierda(contadorAltasOBajas.toString(), 7)
                                        + Util.rellenarConCerosIzquierda(contadorImportes.toString().replaceAll("\\.", ""), 17)
                                        + Util.rellenarConCerosIzquierda("", 7)
                                        + Util.rellenarConCerosIzquierda("", 17)
                                        + Util.rellenarConCerosIzquierda("", 195)
                                        + Util.rellenarConEspaciosDerecha("", 123));
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA + Constants.TIPO_REGISTRO_TA
                                + Util.rellenarConCerosIzquierda(contadorAltasOBajas.toString(), 7)
                                + Util.rellenarConCerosIzquierda(contadorImportes.toString().replaceAll("\\.", ""), 17)
                                + Util.rellenarConCerosIzquierda("", 7)
                                + Util.rellenarConCerosIzquierda("", 17)
                                + Util.rellenarConCerosIzquierda("", 195)
                                + Util.rellenarConEspaciosDerecha("", 123));
                    }else{
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA + Constants.TIPO_REGISTRO_TB
                                + Util.rellenarConCerosIzquierda("", 7)
                                + Util.rellenarConCerosIzquierda("", 17)
                                + Util.rellenarConCerosIzquierda(contadorAltasOBajas.toString(), 7)
                                + Util.rellenarConCerosIzquierda(contadorImportes.toString().replaceAll("\\.", ""), 17)
                                + Util.rellenarConCerosIzquierda("", 195)
                                + Util.rellenarConEspaciosDerecha("", 123));
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA + Constants.TIPO_REGISTRO_TA
                                + Util.rellenarConCerosIzquierda("", 7)
                                + Util.rellenarConCerosIzquierda("", 17)
                                + Util.rellenarConCerosIzquierda(contadorAltasOBajas.toString(), 7)
                                + Util.rellenarConCerosIzquierda(contadorImportes.toString().replaceAll("\\.", ""), 17)
                                + Util.rellenarConCerosIzquierda("", 195)
                                + Util.rellenarConEspaciosDerecha("", 123));
                    }


                    writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA + Constants.TIPO_REGISTRO_TA );


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Exportación Exitosa");
                    alert.setHeaderText(null);
                    alert.setContentText("Los datos se han guardado en el archivo: " + file.getAbsolutePath());
                    alert.showAndWait();
                } catch (IOException e) {
                    // 5. Manejar posibles errores
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error de Exportación");
                    alert.setHeaderText("No se pudo guardar el archivo.");
                    alert.setContentText("Ocurrió un error al intentar escribir los datos. Por favor, inténtelo de nuevo.");
                    alert.showAndWait();
                }
            }
        }else{
            System.out.println("Se omitió exportación por falta de información");
        }
    }

    public void imprimeValores() {
        ObservableList<SistemaDispersionData> dataList = tableViewDispersionFondos.getItems();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyyMMdd");
        for (SistemaDispersionData data : dataList) {
            System.out.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA);
            System.out.println(Constants.TIPO_REGISTRO_DA);
            System.out.println(data.getFormaPago());
            System.out.println("------------------------>" + Util.rellenarConCerosIzquierda(comboBoxPaymentCurrency.getValue().getId(), 2));
            System.out.println(Util.rellenarConCerosIzquierda(data.getImportePago().replaceAll("\\.", ""), 15));
            System.out.println(datePicketFechaAplicacion.getValue().format(formato));
            System.out.println("2------------------------>" +Util.rellenarConCerosIzquierda(comboBoxPaymentConcept.getValue().getId(), 2));
            System.out.println(Util.rellenarConEspaciosDerecha(data.getClaveBeneficiario(), 20));
            //Este se debe de validar
            System.out.println(data.getRfcBeneficiario());
            System.out.println(Util.rellenarConEspaciosDerecha(data.getNombreBeneficiario(), 40));
            System.out.println(Util.rellenarConCerosIzquierda(data.getReferenciaPago(), 16));
            System.out.println(Util.rellenarConCerosIzquierda("", 10));
            System.out.println(Util.rellenarConCerosIzquierda(data.getCuenta(), 20));
            System.out.println(Util.rellenarConCerosIzquierda("", 5));
            System.out.println(Util.rellenarConEspaciosDerecha("", 40));
            System.out.println(data.getTipoCuenta());
            System.out.println(Util.rellenarConEspaciosDerecha("", 1));
            System.out.println(Util.rellenarConCerosIzquierda("", 5));
            System.out.println(Constants.CLAVE_BANCO_EMISOR);
            System.out.println(Util.rellenarConCerosIzquierda(data.getBancoReceptor(), 5));
            System.out.println(Util.rellenarConCerosIzquierda(data.getDiasVigencia(), 3));
            System.out.println(Util.rellenarConEspaciosDerecha(data.getConceptoPago(), 50));
            System.out.println(Util.rellenarConEspaciosDerecha(data.getInfoAgruparPagos(), 60));
            System.out.println(Util.rellenarConCerosIzquierda("", 25));
            System.out.println(Util.rellenarConEspaciosDerecha(data.getInfoAgruparPagos(), 16));
        }
    }

    private boolean validateForm() {
        boolean allFieldsAreValid = true;

        // Limpiar estilos y mensajes de error de todos los campos
        textFieldClientNumber.getStyleClass().remove("error-field");
        textFieldFileNumberOfTheDay.getStyleClass().remove("error-field");
        textFieldChargeAccount.getStyleClass().remove("error-field");
        textFieldCompanyReference.getStyleClass().remove("error-field");

        errorLabelClientNumber.setVisible(false);
        errorLabelFileNumberOfTheDay.setVisible(false);
        errorLabelChargeAccount.setVisible(false);
        errorLabelCompanyReference.setVisible(false);

        // Validar TextFields
        if (textFieldClientNumber.getText().trim().isEmpty()) {
            textFieldClientNumber.getStyleClass().add("error-field");
            errorLabelClientNumber.setText("Este campo es requerido.");
            errorLabelClientNumber.setVisible(true);
            allFieldsAreValid = false;
        }
        if (textFieldFileNumberOfTheDay.getText().trim().isEmpty()) {
            textFieldFileNumberOfTheDay.getStyleClass().add("error-field");
            errorLabelFileNumberOfTheDay.setText("Este campo es requerido.");
            errorLabelFileNumberOfTheDay.setVisible(true);
            allFieldsAreValid = false;
        }
        if (textFieldChargeAccount.getText().trim().isEmpty()) {
            textFieldChargeAccount.getStyleClass().add("error-field");
            errorLabelChargeAccount.setText("Este campo es requerido.");
            errorLabelChargeAccount.setVisible(true);
            allFieldsAreValid = false;
        }
        if (textFieldCompanyReference.getText().trim().isEmpty()) {
            textFieldCompanyReference.getStyleClass().add("error-field");
            errorLabelCompanyReference.setText("Este campo es requerido.");
            errorLabelCompanyReference.setVisible(true);
            allFieldsAreValid = false;
        }

        // Validar la tabla (sin mostrar la alerta aquí)
        StringBuilder errorMessage = new StringBuilder("Por favor, llena los campos obligatorios:\n\n");
        boolean hasTableErrors = false;

        // Validar campos de la tabla
        int rowIndex = 0;
        for (SistemaDispersionData data : tableViewDispersionFondos.getItems()) {
            rowIndex++;

            // Forma de Pago
            if (data.getFormaPago() == null || data.getFormaPago().trim().isEmpty()) {
                errorMessage.append("• Columna 'Forma de Pago' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Tipo de Cuenta
            if (data.getTipoCuenta() == null || data.getTipoCuenta().trim().isEmpty()) {
                errorMessage.append("• Columna 'Tipo de Cuenta' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Banco Receptor
            if (data.getBancoReceptor() == null || data.getBancoReceptor().trim().isEmpty()) {
                errorMessage.append("• Columna 'Banco Receptor' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Cuenta
            if (data.getCuenta() == null || data.getCuenta().trim().isEmpty()) {
                errorMessage.append("• Columna 'Cuenta' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Importe Pago
            if (data.getImportePago() == null || data.getImportePago().trim().isEmpty()) {
                errorMessage.append("• Columna 'Importe de Pago' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Clave Beneficiario
            if (data.getClaveBeneficiario() == null || data.getClaveBeneficiario().trim().isEmpty()) {
                errorMessage.append("• Columna 'Clave Beneficiario' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // RFC Beneficiario
            if (data.getRfcBeneficiario() == null || data.getRfcBeneficiario().trim().isEmpty()) {
                errorMessage.append("• Columna 'RFC Beneficiario' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Nombre Beneficiario
            if (data.getNombreBeneficiario() == null || data.getNombreBeneficiario().trim().isEmpty()) {
                errorMessage.append("• Columna 'Nombre Beneficiario' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Referencia de Pago
            if (data.getReferenciaPago() == null || data.getReferenciaPago().trim().isEmpty()) {
                errorMessage.append("• Columna 'Referencia de Pago' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Concepto de Pago
            if (data.getConceptoPago() == null || data.getConceptoPago().trim().isEmpty()) {
                errorMessage.append("• Columna 'Concepto de Pago' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Dias Vigencia
            if (data.getDiasVigencia() == null || data.getDiasVigencia().trim().isEmpty()) {
                errorMessage.append("• Columna 'Dias Vigencia' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Detalle Mail
            if (data.getDetalleMail() == null || data.getDetalleMail().trim().isEmpty()) {
                errorMessage.append("• Columna 'Detalle Mail' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Referencia abono banxico y tipo operación
            if (comboBoxPaymentCurrency.getValue().getId() == "01") {
                if(data.getReferenciaAbonoBanxico() == null || data.getReferenciaAbonoBanxico().trim().isEmpty()){
                    errorMessage.append("• Columna 'Referencia abono banxico' del registro ").append(rowIndex).append("\n");
                    allFieldsAreValid = false;
                    hasTableErrors = true;
                }
                if(data.getTipoOperacion() == null || data.getTipoOperacion().trim().isEmpty()){
                    errorMessage.append("• Columna 'Tipo operación' del registro ").append(rowIndex).append("\n");
                    allFieldsAreValid = false;
                    hasTableErrors = true;
                }
            }
        }

        // Si hay errores en la tabla, muestra la alerta con la lista completa
        if (hasTableErrors) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error de Validaci\u00f3n");
            alert.setHeaderText("Campos de la tabla incompletos.");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
        return allFieldsAreValid;
    }



}