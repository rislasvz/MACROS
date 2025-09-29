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
import org.scotiabank.productosGTB.enums.MaintOrConsultationEnum;
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

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class PreRegistroCuentasController {

    private static final Logger log = LoggerFactory.getLogger(PreRegistroCuentasController.class);
    @FXML
    private TextField textFieldContractNumber;
    @FXML
    private ComboBox<MaintOrConsultationEnum> comboBoxMaintOrConsultation;
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
    private Label errorLabelFechaDeSolicitud;
    @FXML
    private Label errorLabelFileNumberOfTheDay;

    @FXML
    public void initialize() {
        agregaTooltips();
        restringeTextField();
        fillAllComboBox();
        dataList = FXCollections.observableArrayList(
                new PreRegistroCuentasData(1, "", "", "", "", "", "", "", "", "")
        );
        tableViewPreRegistroCuentas.setItems(dataList);
        fillTable();
    }

    public void fillAllComboBox(){
        comboBoxMaintOrConsultation.setItems(FXCollections.observableArrayList(MaintOrConsultationEnum.values()));
        comboBoxMaintOrConsultation.getSelectionModel().selectFirst();
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
        tableViewPreRegistroCuentas.refresh();
    }

    @FXML
    private void eliminarFila(ActionEvent event) {
        // Solo elimina la fila si hay más de una
        if (dataList.size() > 1) {
            dataList.removeLast();
            tableViewPreRegistroCuentas.refresh();
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
        datePicketFechaDeSolicitud.getStyleClass().remove("error-field");

        errorLabelContractNumber.setVisible(false);
        errorLabelFileNumberOfTheDay.setVisible(false);
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

    public void exportarDatosAArchivoOptimizada() {
        tableViewPreRegistroCuentas.refresh();
        //if (true) {
        if (validateForm()) {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyyMMdd");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Archivo de Datos");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                try (
                        FileWriter fw = new FileWriter(file);
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter writer = new PrintWriter(bw)
                ) {
                    int contadorAltasOBajas = 0;
                    double contadorImportes = 0.0;
                    //Escribimos las dos primeras lineas del txt
                    writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                            + Constants.TIPO_REGISTRO_HA
                            + Constants.DISPERSION_DE_FONDOS
                            + Util.rellenarConCerosIzquierda(textFieldContractNumber.getText(), 7)
                            + datePicketFechaDeSolicitud.getValue().format(formato)
                            //VALIDAR SI ESTE ESTA CORRECTO
                            + Util.rellenarConCerosIzquierda(textFieldFileNumberOfTheDay.getText(), 3)
                            + comboBoxMaintOrConsultation.getValue().getId()
                            + Util.rellenarConCerosIzquierda("", 8)
                            + Util.rellenarConCerosIzquierda("", 6)
                            + Util.rellenarConCerosIzquierda("", 3)
                            + Util.rellenarConEspaciosDerecha("", 97));

                    for (PreRegistroCuentasData data : dataList) {
                        //Sumamos uno al contador de registors que se han hecho
                        contadorAltasOBajas++;

                        //Logica para los importes
                        /*DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                        symbols.setDecimalSeparator('.');
                        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
                        decimalFormat.setParseBigDecimal(true);
                        try {
                            String cleanedString = data.getImportePago().replaceAll(",", "");
                            Number parsedNumber = decimalFormat.parse(cleanedString);
                            contadorImportes = contadorImportes + parsedNumber.doubleValue();
                        } catch (ParseException e) {
                            System.err.println("Error al parsear el importe");
                        }*/

                        //PRIMERA LINEA QUE SE REPITE POR CADA REGISTRO DE LA TABLA
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                                + Constants.TIPO_REGISTRO_DR
                                + "A"
                                + Util.rellenarConCerosIzquierda(data.getTipoCuenta(), 2)
                                + Util.rellenarConCerosIzquierda(data.getMonedaCuenta(), 2)
                                + Util.rellenarConCerosIzquierda(data.getClaveBanco(), 3)
                                + Util.rellenarConCerosIzquierda(data.getCuentaAbono(), 20)
                                + Util.rellenarConCerosIzquierda(data.getFechaEliminacion(), 8)
                                + Util.rellenarConEspaciosDerecha(data.getDescripcion(), 50)
                                + Util.rellenarConCerosIzquierda(data.getLimiteTransaccion().replaceAll("\\.", ""), 8)
                                + Util.rellenarConCerosIzquierda(data.getTipoCuenta(), 2));
                    }
                    /*if(Objects.equals(comboBoxFileType.getValue().getId(), "1")){
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                                + Constants.TIPO_REGISTRO_TB
                                + Util.rellenarConCerosIzquierda(Integer.toString(contadorAltasOBajas), 7)
                                + Util.rellenarConCerosIzquierda(Double.toString(contadorImportes).replaceAll("\\.", ""), 17)
                                + Util.rellenarConCerosIzquierda("", 7)
                                + Util.rellenarConCerosIzquierda("", 17)
                                + Util.rellenarConCerosIzquierda("", 195)
                                + Util.rellenarConEspaciosDerecha("", 123));
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA + Constants.TIPO_REGISTRO_TA
                                + Util.rellenarConCerosIzquierda(Integer.toString(contadorAltasOBajas), 7)
                                + Util.rellenarConCerosIzquierda(Double.toString(contadorImportes).replaceAll("\\.", ""), 17)
                                + Util.rellenarConCerosIzquierda("", 7)
                                + Util.rellenarConCerosIzquierda("", 17)
                                + Util.rellenarConCerosIzquierda("", 195)
                                + Util.rellenarConEspaciosDerecha("", 123));
                    }else{
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA + Constants.TIPO_REGISTRO_TB
                                + Util.rellenarConCerosIzquierda("", 7)
                                + Util.rellenarConCerosIzquierda("", 17)
                                + Util.rellenarConCerosIzquierda(Integer.toString(contadorAltasOBajas), 7)
                                + Util.rellenarConCerosIzquierda(Double.toString(contadorImportes).replaceAll("\\.", ""), 17)
                                + Util.rellenarConCerosIzquierda("", 195)
                                + Util.rellenarConEspaciosDerecha("", 123));
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA + Constants.TIPO_REGISTRO_TA
                                + Util.rellenarConCerosIzquierda("", 7)
                                + Util.rellenarConCerosIzquierda("", 17)
                                + Util.rellenarConCerosIzquierda(Integer.toString(contadorAltasOBajas), 7)
                                + Util.rellenarConCerosIzquierda(Double.toString(contadorImportes).replaceAll("\\.", ""), 17)
                                + Util.rellenarConCerosIzquierda("", 195)
                                + Util.rellenarConEspaciosDerecha("", 123));
                    }*/
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

    public void restringeTextField(){
        //Validaciones de textfield
        //Validacion solo numeros y maximo de caracteres
        textFieldContractNumber.textProperty().addListener((obs, oldValue, newValue) -> {
            TextFieldValidator.validarNumerosYMaxLength(textFieldContractNumber, 7);
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
    }

    public void imprimeValores() {
        ObservableList<PreRegistroCuentasData> dataList = tableViewPreRegistroCuentas.getItems();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyyMMdd");
        for (PreRegistroCuentasData data : dataList) {

        }
    }

    public void agregaTooltips(){
        TooltipManager.applyTooltip(textFieldContractNumber, TooltipMessages.CONTRACT_NUMBER_TOOLTIP);
        TooltipManager.applyTooltip(textFieldFileNumberOfTheDay, TooltipMessages.FILE_NUMBRER_OF_THE_DAY_TOOLTIP);
    }
}
