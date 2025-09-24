package org.scotiabank.productosGTB.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import org.scotiabank.productosGTB.data.TooltipMessages;
import org.scotiabank.productosGTB.enums.FileTypesEnum;
import org.scotiabank.productosGTB.enums.PaymentConceptEnum;
import org.scotiabank.productosGTB.enums.PaymentCurrencyEnum;
import org.scotiabank.productosGTB.model.PreRegistroCuentasData;
import org.scotiabank.productosGTB.model.SistemaDispersionData;
import org.scotiabank.productosGTB.util.Constants;
import org.scotiabank.productosGTB.util.TextFieldValidator;
import org.scotiabank.productosGTB.util.TooltipManager;
import org.scotiabank.productosGTB.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class PreRegistroCuentasController {

    private static final Logger log = LoggerFactory.getLogger(PreRegistroCuentasController.class);
    @FXML
    private TextField textFieldContractNumber;
    @FXML
    private TextField textFieldMaintOrConsultation;
    @FXML
    private TextField textFieldFileNumberOfTheDay;
    @FXML
    private DatePicker datePicketFechaDeSolicitud;
    @FXML
    private TableView<PreRegistroCuentasData> tableViewPreRegistroCuentas;
    private ObservableList<PreRegistroCuentasData> dataList;
    @FXML
    private TableColumn<PreRegistroCuentasData, Integer> num;
    @FXML
    private TableColumn<PreRegistroCuentasData, String> tipoRegistro;
    @FXML
    private TableColumn<PreRegistroCuentasData, String> tipoPago;
    @FXML
    private TableColumn<PreRegistroCuentasData, String> tipoCuenta;
    @FXML
    private TableColumn<PreRegistroCuentasData, String> monedaCuenta;
    @FXML
    private TableColumn<PreRegistroCuentasData, String> claveBanco;
    @FXML
    private TableColumn<PreRegistroCuentasData, String> cuentaAbono;
    @FXML
    private TableColumn<PreRegistroCuentasData, String> limiteTransaccion;
    @FXML
    private TableColumn<PreRegistroCuentasData, String> fechaEliminacion;
    @FXML
    private TableColumn<PreRegistroCuentasData, String> descripcion;

    @FXML
    private Label errorLabelContractNumber;
    @FXML
    private Label errorLabelMaintOrConsultation;
    @FXML
    private Label errorLabelFechaDeSolicitud;
    @FXML
    private Label errorLabelFileNumberOfTheDay;

    @FXML
    public void initialize() {
        agregaTooltips();
        restringeTextField();

        dataList = FXCollections.observableArrayList(
                new PreRegistroCuentasData(1, "", "", "", "", "", "", "", "", "")
        );
        tableViewPreRegistroCuentas.setItems(dataList);
        fillTable();
    }

    public void fillTable(){
        tipoRegistro.setCellValueFactory(new PropertyValueFactory<>("tipoRegistro"));
        tipoPago.setCellValueFactory(new PropertyValueFactory<>("tipoPago"));
        tipoCuenta.setCellValueFactory(new PropertyValueFactory<>("tipoCuenta"));
        monedaCuenta.setCellValueFactory(new PropertyValueFactory<>("monedaCuenta"));
        claveBanco.setCellValueFactory(new PropertyValueFactory<>("claveBanco"));
        cuentaAbono.setCellValueFactory(new PropertyValueFactory<>("cuentaAbono"));
        limiteTransaccion.setCellValueFactory(new PropertyValueFactory<>("limiteTransaccion"));
        fechaEliminacion.setCellValueFactory(new PropertyValueFactory<>("fechaEliminacion"));
        descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        num.setCellValueFactory(new PropertyValueFactory<>("num"));
        tipoRegistro.setCellFactory(Util.createPersonalizedCellFactory(Arrays.asList("DA", "DC", "DB", "DQ")));
        tipoPago.setCellFactory(Util.createPersonalizedCellFactory(Arrays.asList("01", "02", "03", "04")));
        tipoCuenta.setCellFactory(Util.createPersonalizedCellFactory(Arrays.asList("01", "02", "03", "04", "09")));
        monedaCuenta.setCellFactory(Util.createPersonalizedCellFactory(Arrays.asList("00", "01")));
        claveBanco.setCellFactory(Util.createNumericCellFactory(1, 3));
        cuentaAbono.setCellFactory(Util.createNumericCellFactory(11, 18));
        //DECIMAL
        limiteTransaccion.setCellFactory(Util.createDecimalCellFactory(1, 15));
        fechaEliminacion.setCellFactory(Util.createNumericCellFactory(8, 8));
        descripcion.setCellFactory(Util.createStringWithoutSymbolsCellFactory(1, 50));
        tableViewPreRegistroCuentas.setEditable(true);
    }

    @FXML
    private void agregarFila(ActionEvent event) {
        Integer nuevoNumero = dataList.size() + 1;
        PreRegistroCuentasData nuevaFila = new PreRegistroCuentasData(
                nuevoNumero, "", "", "", "", "", "", "", "", ""
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

    @FXML
    private boolean validateForm() {
        boolean allFieldsAreValid = true;

        // Limpiar estilos y mensajes de error de todos los campos
        textFieldContractNumber.getStyleClass().remove("error-field");
        textFieldFileNumberOfTheDay.getStyleClass().remove("error-field");
        textFieldMaintOrConsultation.getStyleClass().remove("error-field");
        datePicketFechaDeSolicitud.getStyleClass().remove("error-field");

        errorLabelContractNumber.setVisible(false);
        errorLabelFileNumberOfTheDay.setVisible(false);
        errorLabelMaintOrConsultation.setVisible(false);
        errorLabelFechaDeSolicitud.setVisible(false);

        // Validar TextFields
        if (textFieldContractNumber.getText().trim().isEmpty()) {
            textFieldContractNumber.getStyleClass().add("error-field");
            errorLabelContractNumber.setText("Este campo es requerido.");
            errorLabelContractNumber.setVisible(true);
            allFieldsAreValid = false;
        }
        if (textFieldFileNumberOfTheDay.getText().trim().isEmpty()) {
            textFieldFileNumberOfTheDay.getStyleClass().add("error-field");
            errorLabelFileNumberOfTheDay.setText("Este campo es requerido.");
            errorLabelFileNumberOfTheDay.setVisible(true);
            allFieldsAreValid = false;
        }
        if (textFieldMaintOrConsultation.getText().trim().isEmpty()) {
            textFieldMaintOrConsultation.getStyleClass().add("error-field");
            errorLabelMaintOrConsultation.setText("Este campo es requerido.");
            errorLabelMaintOrConsultation.setVisible(true);
            allFieldsAreValid = false;
        }
        if (datePicketFechaDeSolicitud.getValue() == null) {
            datePicketFechaDeSolicitud.getStyleClass().add("error-field");
            errorLabelFechaDeSolicitud.setText("Este campo es requerido.");
            errorLabelFechaDeSolicitud.setVisible(true);
            allFieldsAreValid = false;
        }

        // Validar la tabla (sin mostrar la alerta aquí)
        StringBuilder errorMessage = new StringBuilder("Por favor, llena los campos obligatorios:\n\n");
        boolean hasTableErrors = false;

        // Validar campos de la tabla
        int rowIndex = 0;
        for (PreRegistroCuentasData data : tableViewPreRegistroCuentas.getItems()) {
            rowIndex++;

            // Tipo Registro
            if (data.getTipoRegistro() == null || data.getTipoRegistro().trim().isEmpty()) {
                errorMessage.append("• Columna 'Tipo Registro' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Tipo Pago
            if (data.getTipoPago() == null || data.getTipoPago().trim().isEmpty()) {
                errorMessage.append("• Columna 'Tipo Pago' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Tipo Cuenta
            if (data.getTipoCuenta() == null || data.getTipoCuenta().trim().isEmpty()) {
                errorMessage.append("• Columna 'Tipo Cuenta' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Moneda Cuenta
            if (data.getMonedaCuenta() == null || data.getMonedaCuenta().trim().isEmpty()) {
                errorMessage.append("• Columna 'Cuenta' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Clave Banco
            if (data.getClaveBanco() == null || data.getClaveBanco().trim().isEmpty()) {
                errorMessage.append("• Columna 'Clave Banco' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Cuenta Abono
            if (data.getCuentaAbono() == null || data.getCuentaAbono().trim().isEmpty()) {
                errorMessage.append("• Columna 'Cuenta Abono' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Limite Transaccion
            if (data.getLimiteTransaccion() == null || data.getLimiteTransaccion().trim().isEmpty()) {
                errorMessage.append("• Columna 'Limite Transaccion' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Fecha Eliminacion
            if (data.getFechaEliminacion() == null || data.getFechaEliminacion().trim().isEmpty()) {
                errorMessage.append("• Columna 'Fecha Eliminacion' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
            }
            // Descripcion
            if (data.getDescripcion() == null || data.getDescripcion().trim().isEmpty()) {
                errorMessage.append("• Columna 'Descripcion' del registro ").append(rowIndex).append("\n");
                allFieldsAreValid = false;
                hasTableErrors = true;
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

    public void restringeTextField(){
        //Validaciones de textfield
        //Validacion solo numeros y maximo de caracteres
        textFieldContractNumber.textProperty().addListener((obs, oldValue, newValue) -> {
            TextFieldValidator.validarNumerosYMaxLength(textFieldContractNumber, 12);
        });

        // Validación al perder el foco: longitud mínima
        textFieldContractNumber.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                TextFieldValidator.validarMinLength(textFieldContractNumber, 1);
            }
        });

        //Validacion solo numeros y maximo de caracteres
        textFieldFileNumberOfTheDay.textProperty().addListener((obs, oldValue, newValue) -> {
            TextFieldValidator.validarNumerosYMaxLength(textFieldFileNumberOfTheDay, 2);
        });

        // Validación al perder el foco: longitud mínima
        textFieldFileNumberOfTheDay.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                TextFieldValidator.validarMinLength(textFieldFileNumberOfTheDay, 1);
            }
        });

        //Validacion solo numeros y maximo de caracteres
        textFieldMaintOrConsultation.textProperty().addListener((obs, oldValue, newValue) -> {
            TextFieldValidator.validarNumerosYMaxLength(textFieldMaintOrConsultation, 10);
        });

        // Validación al perder el foco: longitud mínima
        textFieldMaintOrConsultation.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                TextFieldValidator.validarMinLength(textFieldMaintOrConsultation, 10);
            }
        });
    }

    public void imprimeValores() {
        ObservableList<PreRegistroCuentasData> dataList = tableViewPreRegistroCuentas.getItems();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyyMMdd");
        for (PreRegistroCuentasData data : dataList) {

        }
    }

    public void agregaTooltips(){
        TooltipManager.applyTooltip(textFieldContractNumber, TooltipMessages.CONTRACT_NUMBER_TOOLTIP);
        TooltipManager.applyTooltip(textFieldMaintOrConsultation, TooltipMessages.MAIN_OR_CONSULTATION_TOOLTIP);
        TooltipManager.applyTooltip(textFieldFileNumberOfTheDay, TooltipMessages.FILE_NUMBRER_OF_THE_DAY_TOOLTIP);
    }
}
