package org.scotiabank.productosGTB.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.scotiabank.productosGTB.business.Service;
import org.scotiabank.productosGTB.data.ErrorData;
import org.scotiabank.productosGTB.data.TooltipMessages;
import org.scotiabank.productosGTB.enums.FileTypesEnum;
import org.scotiabank.productosGTB.enums.PaymentConceptEnum;
import org.scotiabank.productosGTB.enums.PaymentCurrencyEnum;
import org.scotiabank.productosGTB.model.SistemaDispersionData;
import org.scotiabank.productosGTB.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private Label errorLabelFechaAplicacion;
    @FXML
    private Button btnCargar;

    private boolean campoEditado = false;
    private final ObservableList<SistemaDispersionData> datos = FXCollections.observableArrayList();
    private boolean cargarDesdeExcel = false;


    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            tableViewDispersionFondos.getScene().getRoot().requestFocus();
        });

        tableViewDispersionFondos.setOnKeyPressed(event -> {
            // Verifica si se presionó la combinación de teclas Ctrl + V
            if (event.isControlDown() && event.getCode() == KeyCode.V) {
                pasteFromClipboard();
            }
        });

        restringeTextField();
        Util.limitarFechas(datePicketFechaAplicacion);
        fillAllComboBox();
        agregaTooltips();

        //tableViewDispersionFondos.setItems(datos);
        Platform.runLater(() -> {
            // Siempre agrega el registro vacío
            dataList = FXCollections.observableArrayList(
                    new SistemaDispersionData(1, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
            );
            tableViewDispersionFondos.setItems(dataList);

            // Configura el botón de carga
            btnCargar.setOnAction(e -> importarExcel());

            // Si se indicó que se debe cargar Excel, ejecuta el método
            if (cargarDesdeExcel) {
                importarExcel();
            }
        });


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
        formaPago.setCellFactory(Util.createPersonalizedCellFactory(Arrays.asList("1", "2", "3", "4", "10")));
        tipoCuenta.setCellFactory(Util.createNumericCellFactory(1, 1));
        bancoReceptor.setCellFactory(Util.createNumericCellFactory(3, 3));
        cuenta.setCellFactory(Util.createNumericCellFactory(11, 20));
        importePago.setCellFactory(Util.createDecimalCellFactory(1, 13));
        claveBeneficiario.setCellFactory(Util.createStringWithoutSymbolsCellFactory(1, 20));
        rfcBeneficiario.setCellFactory(Util.createStringWithoutSymbolsCellFactory(13, 13));
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
        tableViewDispersionFondos.refresh();
    }

    @FXML
    private void eliminarFila(ActionEvent event) {
        // Solo elimina la fila si hay más de una
        if (dataList.size() > 1) {
            dataList.removeLast();
            tableViewDispersionFondos.refresh();
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
                    String numClienteCompleto;
                    String fillerLinea1;
                    if(textFieldClientNumber.getText().length() >= 6 ){
                        numClienteCompleto = Util.rellenarConCerosIzquierda(textFieldClientNumber.getText(), 12);
                        fillerLinea1 = Util.rellenarConCerosIzquierda("", 332);
                    }else{
                        numClienteCompleto = Util.rellenarConCerosIzquierda(textFieldClientNumber.getText(), 5);
                        fillerLinea1 = Util.rellenarConCerosIzquierda("", 325);
                    }
                    //Escribimos las dos primeras lineas del txt
                    writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                            + Constants.TIPO_REGISTRO_HA
                            + numClienteCompleto
                            + Util.rellenarConCerosIzquierda(textFieldFileNumberOfTheDay.getText(), 2)
                            +"000000000000000000000000000" + fillerLinea1);
                    writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                            + Constants.TIPO_REGISTRO_HB
                            + Util.rellenarConCerosIzquierda(comboBoxPaymentCurrency.getValue().getId(), 2)
                            + "0000" + Util.rellenarConCerosIzquierda(textFieldChargeAccount.getText(), 11)
                            + Util.rellenarConCerosIzquierda(textFieldCompanyReference.getText(), 10) + "000"
                            + Util.rellenarConEspaciosDerecha("", 336));

                    for (SistemaDispersionData data : dataList) {
                        //Sumamos uno al contador de registors que se han hecho
                        contadorAltasOBajas++;

                        //Logica para los importes
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                        symbols.setDecimalSeparator('.');
                        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
                        decimalFormat.setParseBigDecimal(true);
                        try {
                            String cleanedString = data.getImportePago().replaceAll(",", "");
                            Number parsedNumber = decimalFormat.parse(cleanedString);
                            contadorImportes = contadorImportes + parsedNumber.doubleValue();
                        } catch (ParseException e) {
                            System.err.println("Error al parsear el importe");
                        }

                        //contadorImportes = Double.valueOf(contadorImportes + data.getImportePago());
                        //PRIMERA LINEA QUE SE REPITE POR CADA REGISTRO DE LA TABLA
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                                + Constants.TIPO_REGISTRO_DA
                                + Util.rellenarConCerosIzquierda(data.getFormaPago(), 2)
                                + Util.rellenarConCerosIzquierda(comboBoxPaymentCurrency.getValue().getId(), 2)
                                + Util.rellenarConCerosIzquierda(data.getImportePago().replaceAll("\\.", ""), 15)
                                + datePicketFechaAplicacion.getValue().format(formato)
                                + Util.rellenarConCerosIzquierda(comboBoxPaymentConcept.getValue().getId(), 2)
                                + Util.rellenarConEspaciosDerecha(data.getClaveBeneficiario(), 20)
                                + Util.rellenarConCerosIzquierda(data.getRfcBeneficiario(), 13)
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
                                + Util.rellenarConCerosIzquierda(data.getBancoReceptor(), 3)
                                + Util.rellenarConCerosIzquierda(data.getDiasVigencia(), 3)
                                + Util.rellenarConEspaciosDerecha(data.getConceptoPago(), 50)
                                + Util.rellenarConEspaciosDerecha(data.getInfoAgruparPagos(), 60)
                                + Util.rellenarConCerosIzquierda("", 25)
                                + Util.rellenarConEspaciosDerecha("", 16)
                                //Filler
                                + Util.rellenarConEspaciosDerecha("", 6));
                        //SEGUNDA LINEA DEL TXT
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA + Constants.TIPO_REGISTRO_DM + data.getDetalleMail());
                    }
                    if(Objects.equals(comboBoxFileType.getValue().getId(), "1")){
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
                    }
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
        datePicketFechaAplicacion.getStyleClass().remove("error-field");

        errorLabelClientNumber.setVisible(false);
        errorLabelFileNumberOfTheDay.setVisible(false);
        errorLabelChargeAccount.setVisible(false);
        errorLabelCompanyReference.setVisible(false);
        errorLabelFechaAplicacion.setVisible(false);

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
        if (datePicketFechaAplicacion.getValue() == null) {
            datePicketFechaAplicacion.getStyleClass().add("error-field");
            errorLabelFechaAplicacion.setText("Este campo es requerido.");
            errorLabelFechaAplicacion.setVisible(true);
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
            if (Objects.equals(comboBoxPaymentCurrency.getValue().getId(), "01")) {
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
            alert.setHeaderText("Campos de la tabla incompletos. Desplázate para ver todos los errores:");

            // 1. Crea un TextArea y establece el contenido del error
            TextArea textArea = new TextArea(errorMessage.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);

            // 2. Establece un tamaño preferido para limitar la altura visible
            // Esto asegura que el Alert tenga un tamaño fijo y manejable.
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            textArea.setPrefHeight(300); // <-- Altura máxima que quieres para la lista

            // 3. Establece el TextArea como el contenido expandible del diálogo
            alert.getDialogPane().setExpandableContent(textArea);
            alert.getDialogPane().setExpanded(true); // Opcional, forzar que se muestre expandido por defecto.

            // Nota: Para este caso simple, puedes establecer directamente el TextArea como Content:
            // alert.getDialogPane().setContent(textArea);
            // Pero usar setExpandableContent es una buena práctica para listas largas.

            alert.showAndWait();
        }
        return allFieldsAreValid;
    }

    private void pasteFromClipboard() {
        // 1. Obtiene el contenido del portapapeles como una cadena de texto
        Clipboard clipboard = Clipboard.getSystemClipboard();
        String clipboardContent = clipboard.getString();

        // Si el portapapeles está vacío, no hace nada
        if (clipboardContent == null || clipboardContent.trim().isEmpty()) {
            return;
        }

        // 2. Divide el contenido en filas (cada fila es una línea en el texto)
        String[] rows = clipboardContent.split("\n");

        for (int i = 0; i < rows.length; i++) {
            // 3. Divide cada fila en columnas (valores separados por tabulaciones)
            String[] columns = rows[i].split("\t");

            // 4. Obtiene el objeto de datos para la fila actual o crea uno nuevo si es necesario
            SistemaDispersionData rowData;
            if (tableViewDispersionFondos.getItems().size() > i) {
                rowData = tableViewDispersionFondos.getItems().get(i);
            } else {
                // Crea una nueva fila con valores predeterminados
                rowData = new SistemaDispersionData(
                        tableViewDispersionFondos.getItems().size() + 1, "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
                );
                // Agrega la nueva fila a tu lista de datos
                tableViewDispersionFondos.getItems().add(rowData);
            }

            // 5. Asigna los valores de las columnas a las propiedades del objeto 'rowData'
            // Esto es un ejemplo. Debes asegurarte de que el orden de las columnas sea correcto.
            if (columns.length > 0) rowData.setFormaPago(columns[0]);
            if (columns.length > 1) rowData.setTipoCuenta(columns[1]);
            if (columns.length > 2) rowData.setBancoReceptor(columns[2]);
            if (columns.length > 3) rowData.setCuenta(columns[3]);
            if (columns.length > 4) rowData.setImportePago(columns[4]);
            if (columns.length > 5) rowData.setClaveBeneficiario(columns[5]);
            if (columns.length > 6) rowData.setRfcBeneficiario(columns[6]);
            if (columns.length > 7) rowData.setNombreBeneficiario(columns[7]);
            if (columns.length > 8) rowData.setReferenciaPago(columns[8]);
            if (columns.length > 9) rowData.setConceptoPago(columns[9]);
            if (columns.length > 10) rowData.setDiasVigencia(columns[10]);
            if (columns.length > 11) rowData.setInfoAgruparPagos(columns[11]);
            if (columns.length > 12) rowData.setDetalleMail(columns[12]);
            if (columns.length > 13) rowData.setReferenciaAbonoBanxico(columns[13]);
            if (columns.length > 14) rowData.setTipoOperacion(columns[14]);
        }

        // Asegura que la tabla se actualice visualmente con los nuevos datos
        tableViewDispersionFondos.refresh();
    }

    public void restringeTextField(){
        //Validaciones de textfield
        //Validacion solo numeros y maximo de caracteres
        textFieldClientNumber.textProperty().addListener((obs, oldValue, newValue) -> {
            TextFieldValidator.validarNumerosYMaxLength(textFieldClientNumber, 12);
        });

        // Validación al perder el foco: longitud mínima ESTE VA SIEMPRE EN
        textFieldClientNumber.textProperty().addListener((obs, oldVal, newVal) -> {
            campoEditado = true;
        });
        textFieldClientNumber.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && campoEditado) {
                TextFieldValidator.validarMinLength(textFieldClientNumber, 1);
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
        textFieldCompanyReference.textProperty().addListener((obs, oldValue, newValue) -> {
            TextFieldValidator.validarNumerosYMaxLength(textFieldCompanyReference, 10);
        });

        // Validación al perder el foco: longitud mínima
        textFieldCompanyReference.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                TextFieldValidator.validarMinLength(textFieldCompanyReference, 10);
            }
        });

        //Validacion solo numeros y maximo de caracteres
        textFieldChargeAccount.textProperty().addListener((obs, oldValue, newValue) -> {
            TextFieldValidator.validarNumerosYMaxLength(textFieldChargeAccount, 11);
        });

        // Validación al perder el foco: longitud mínima
        textFieldChargeAccount.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                TextFieldValidator.validarMinLength(textFieldChargeAccount, 11);
            }
        });
    }

    public void setCargarDesdeExcel(boolean cargarDesdeExcel) {
        this.cargarDesdeExcel = cargarDesdeExcel;
    }


    // Dentro de SistemaDispercionFondosController.java

    @FXML
    private void importarExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona archivo Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File archivo = fileChooser.showOpenDialog(btnCargar.getScene().getWindow());

        if (archivo != null) {
            List<ErrorData> erroresDeFormato = new ArrayList<>();

            List<String> allowedFormasPago = Arrays.asList("1", "2", "3", "4", "10");

            List<ColumnaConfig> configuracion = Arrays.asList(
                    new ColumnaConfig("Forma Pago", 0, Util::isValidAllowedValue, "Contiene valores no permitidos", allowedFormasPago),
                    new ColumnaConfig("Tipo de Cuenta", 1, (v, l) -> Util.isValidNumeric(v), "Debe contener solo números.", null),
                    new ColumnaConfig("Banco Receptor", 2, (v, l) -> Util.isValidNumeric(v), "Debe contener solo números.", null),
                    new ColumnaConfig("Cuenta", 3, (v, l) -> Util.isValidNumeric(v), "Debe contener solo números.", null),
                    new ColumnaConfig("Importe Pago", 4, (v, l) -> Util.isValidDecimal(v), "Formato incorrecto (solo números y 2 decimales).", null),
                    new ColumnaConfig("Clave Beneficiario", 5, (v, l) -> Util.isValidStringWithoutSymbols(v), "Contiene símbolos, mayúsculas o acentos no permitidos.", null),
                    new ColumnaConfig("RFC Beneficiario", 6, (v, l) -> Util.isValidStringWithoutSymbols(v), "Contiene símbolos, mayúsculas o acentos no permitidos.", null),
                    new ColumnaConfig("Nombre Beneficiario", 7, (v, l) -> Util.isValidStringWithoutSymbols(v), "Contiene símbolos, mayúsculas o acentos no permitidos.", null),
                    new ColumnaConfig("Referencia Pago", 8, (v, l) -> Util.isValidNumeric(v), "Debe contener solo números.", null),
                    new ColumnaConfig("Concepto Pago", 9, (v, l) -> Util.isValidStringWithoutSymbols(v), "Contiene símbolos, mayúsculas o acentos no permitidos.", null),
                    new ColumnaConfig("Dias Vigencia", 10, (v, l) -> Util.isValidNumeric(v), "Debe contener solo números.", null),
                    new ColumnaConfig("Info Agrupar Pagos", 11, (v, l) -> Util.isValidStringWithoutSymbols(v), "Contiene símbolos, mayúsculas o acentos no permitidos.", null),
                    new ColumnaConfig("Detalle Mail", 12, (v, l) -> Util.isValidEmail(v), "Contiene símbolos o acentos no permitidos.", null),
                    new ColumnaConfig("Referencia Abono Banxico", 13, (v, l) -> Util.isValidNumeric(v), "Debe contener solo números.", null),
                    new ColumnaConfig("Tipo Operacion", 14, (v, l) -> Util.isValidNumeric(v), "Debe contener solo números.", null)
            );

            try (FileInputStream fis = new FileInputStream(archivo);
                 Workbook workbook = WorkbookFactory.create(fis)) {

                Sheet hoja = workbook.getSheetAt(0);
                dataList.clear();

                for (int i = 1; i <= hoja.getLastRowNum(); i++) {
                    Row fila = hoja.getRow(i);
                    if (fila != null) {

                        boolean filaTieneErrores = false;

                        String[] valoresCelda = new String[configuracion.size()];

                        for (ColumnaConfig config : configuracion) {
                            String valor = getCellValue(fila.getCell(config.indice));

                            boolean esValido = config.validador.apply(valor, config.valoresPermitidos);

                            if (!esValido) {
                                erroresDeFormato.add(new ErrorData(i + 1, config.nombre, valor, config.mensajeError));
                                filaTieneErrores = true;
                                valoresCelda[config.indice] = "";
                            } else {
                                valoresCelda[config.indice] = valor;
                            }
                        }
                        if (!filaTieneErrores) {
                            dataList.add(new SistemaDispersionData(
                                    i,
                                    valoresCelda[0], valoresCelda[1], valoresCelda[2], valoresCelda[3], valoresCelda[4],
                                    valoresCelda[5], valoresCelda[6], valoresCelda[7], valoresCelda[8], valoresCelda[9],
                                    valoresCelda[10], valoresCelda[11], valoresCelda[12], valoresCelda[13], valoresCelda[14]
                            ));
                        }
                    }
                }

                if (!erroresDeFormato.isEmpty()) {
                    Util.mostrarAlerta("El excel cargado contiene errores, se descargará un excel con los errores");
                    Util.exportarErroresAExcel(erroresDeFormato);
                }
                tableViewDispersionFondos.refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    @FXML
    private void regresar(ActionEvent event){
        String ruta = "/fxml/DispersionDeFondos/Main/DispersionFondos.fxml";
        String title = "dispersionFondos";
        Service.navegacion(ruta, title, event);
    }


}