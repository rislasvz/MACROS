package org.scotiabank.productosGTB.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.scotiabank.productosGTB.business.Service;
import org.scotiabank.productosGTB.data.ErrorData;
import org.scotiabank.productosGTB.data.TooltipMessages;
import org.scotiabank.productosGTB.enums.MaintOrConsultationEnum;
import org.scotiabank.productosGTB.model.PreRegistroCuentasData;
import org.scotiabank.productosGTB.model.SistemaDispersionData;
import org.scotiabank.productosGTB.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private Button btnCargar;
    @FXML
    private Button btnAgregarFila;
    @FXML
    private Button btnEliminarFila;

    private boolean cargarDesdeExcel = false;
    private boolean campoEditado = false;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            tableViewPreRegistroCuentas.getScene().getRoot().requestFocus();
        });
        agregaTooltips();
        restringeTextField();
        fillAllComboBox();
        Platform.runLater(() -> {
            // Siempre agrega el registro vacío
            dataList = FXCollections.observableArrayList(
                    new PreRegistroCuentasData(1, "", "", "", "", "", "", "", "", "")
            );
            tableViewPreRegistroCuentas.setItems(dataList);
            btnCargar.setOnAction(e -> importarExcel());

            if (cargarDesdeExcel) {
                Util.setTablaEditable(tableViewPreRegistroCuentas, btnAgregarFila, btnEliminarFila, true);
            }
        });
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
    private boolean validateForm(boolean validateAll) {
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
        if(!allFieldsAreValid && cargarDesdeExcel){
            return false;
        }else if(!validateAll){
            return true;
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
        if (validateForm(true)) {
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
                    int contadorAltas = 0;
                    int contadorBajas = 0;
                    int contadorCambios = 0;
                    int contadorConsultas = 0;
                    String numContratoCompleto;
                    String fillerLinea1;

                    if(textFieldContractNumber.getText().length() >= 6 ){
                        numContratoCompleto = Util.rellenarConCerosIzquierda(textFieldContractNumber.getText(), 12);
                        fillerLinea1 = Util.rellenarConCerosIzquierda("", 97);
                    }else{
                        numContratoCompleto = Util.rellenarConCerosIzquierda(textFieldContractNumber.getText(), 7);
                        fillerLinea1 = Util.rellenarConCerosIzquierda("", 94);
                    }
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
                            + Util.rellenarConEspaciosDerecha("", 97)
                            + fillerLinea1);

                    for (PreRegistroCuentasData data : dataList) {
                        //Sumamos uno al contador de registors que se han hecho
                        if(data.getTipoRegistro().equals("DA")){
                            contadorAltas++;
                        } else if (data.getTipoRegistro().equals("DC")) {
                            contadorCambios++;
                        } else if (data.getTipoRegistro().equals("DB")) {
                            contadorBajas++;
                        } else if (data.getTipoRegistro().equals("DQ")) {
                            contadorConsultas++;
                        }

                        //LINEA QUE SE REPITE POR CADA REGISTRO DE LA TABLA
                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                                + data.getTipoRegistro()
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

                        writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                                + Constants.TIPO_REGISTRO_TA
                                + Util.rellenarConCerosIzquierda(Integer.toString(contadorAltas), 8)
                                + Util.rellenarConCerosIzquierda(Integer.toString(contadorCambios), 8)
                                + Util.rellenarConCerosIzquierda(Integer.toString(contadorBajas), 8)
                                + Util.rellenarConCerosIzquierda(Integer.toString(contadorConsultas), 8)

                                + Util.rellenarConCerosIzquierda("", 8)
                                + Util.rellenarConCerosIzquierda("", 8)
                                + Util.rellenarConCerosIzquierda("", 8)
                                + Util.rellenarConCerosIzquierda("", 8)

                                + Util.rellenarConCerosIzquierda("", 8)
                                + Util.rellenarConCerosIzquierda("", 8)
                                + Util.rellenarConCerosIzquierda("", 8)
                                + Util.rellenarConCerosIzquierda("", 8)

                                + Util.rellenarConCerosIzquierda("", 3)
                                + Util.rellenarConCerosIzquierda("", 3)

                                + Util.rellenarConEspaciosDerecha("", 34));

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
            TextFieldValidator.validarNumerosYMaxLength(textFieldContractNumber, 12);
        });

        textFieldContractNumber.textProperty().addListener((obs, oldVal, newVal) -> {
            campoEditado = true;
        });

        // Validación al perder el foco: longitud mínima
        textFieldContractNumber.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && campoEditado) {
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

    @FXML
    private void importarExcel() {
        if(validateForm(false)){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecciona archivo Excel");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File archivo = fileChooser.showOpenDialog(btnCargar.getScene().getWindow());

            if (archivo != null) {
                List<ErrorData> erroresDeFormato = new ArrayList<>();

                List<String> allowedTipoRegistro = Arrays.asList("DA", "DC", "DB", "DQ");
                List<String> allowedTipoPago = Arrays.asList("01", "02", "03", "04");
                List<String> allowedTipoCuenta = Arrays.asList("01", "02", "03", "04", "09");
                List<String> allowedMonedaCuenta = Arrays.asList("00", "01");

                List<ColumnaConfig> configuracion = Arrays.asList(
                        new ColumnaConfig("Tipo Registro", 0, Util::isValidAllowedValue, "Contiene valores no permitidos", allowedTipoRegistro),
                        new ColumnaConfig("Tipo Pago", 1, Util::isValidAllowedValue, "Contiene valores no permitidos", allowedTipoPago),
                        new ColumnaConfig("Tipo Cuenta", 2, Util::isValidAllowedValue, "Contiene valores no permitidos", allowedTipoCuenta),
                        new ColumnaConfig("Moneda Cuenta", 3, Util::isValidAllowedValue, "Contiene valores no permitidos", allowedMonedaCuenta),
                        new ColumnaConfig("Clave Banco", 4, (v, l) -> Util.isValidNumeric(v, 1, 3), "Debe contener solo números o la longitud no es la correcta.", null),
                        new ColumnaConfig("Cuenta Abono", 5, (v, l) -> Util.isValidNumeric(v, 11, 18), "Debe contener solo números o la longitud no es la correcta.", null),
                        new ColumnaConfig("Limite Transaccion", 6, (v, l) -> Util.isValidDecimal(v, 1, 3), "Formato incorrecto (solo números y 2 decimales).", null),
                        new ColumnaConfig("Fecha Eliminacion", 7, (v, l) -> Util.isValidNumeric(v, 8, 8), "Debe contener solo números o la longitud no es la correcta.", null),
                        new ColumnaConfig("Descripcion", 8, (v, l) -> Util.isValidStringWithoutSymbols(v, 0, 50), "Contiene símbolos, mayúsculas o acentos no permitidos o la longitud no es la correcta.", null)
                );
                descripcion.setCellFactory(Util.createStringWithoutSymbolsCellFactory(1, 50));

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
                                dataList.add(new PreRegistroCuentasData(
                                        i,
                                        valoresCelda[0], valoresCelda[1], valoresCelda[2], valoresCelda[3], valoresCelda[4],
                                        valoresCelda[5], valoresCelda[6], valoresCelda[7], valoresCelda[8]
                                ));
                            }
                        }
                    }

                    if (!erroresDeFormato.isEmpty()) {
                        Util.mostrarAlerta("El excel cargado contiene errores, se descargará un excel con los errores");
                        Util.exportarErroresAExcel(erroresDeFormato);
                    }
                    habilitaTabla(false);
                    tableViewPreRegistroCuentas.refresh();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void habilitaTabla(boolean editable) {
        tableViewPreRegistroCuentas.setEditable(!editable);
        if(!editable){
            tableViewPreRegistroCuentas.getStyleClass().add("disabled-overlay");
        }else{
            tableViewPreRegistroCuentas.getStyleClass().remove("disabled-overlay");
        }
        if (btnAgregarFila != null) {
            btnAgregarFila.setDisable(editable);
        }
        if (btnEliminarFila != null) {
            btnEliminarFila.setDisable(editable);
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

    public void agregaTooltips(){
        TooltipManager.applyTooltip(textFieldContractNumber, TooltipMessages.CONTRACT_NUMBER_TOOLTIP);
        TooltipManager.applyTooltip(textFieldFileNumberOfTheDay, TooltipMessages.FILE_NUMBRER_OF_THE_DAY_TOOLTIP);
    }

    public void setCargarDesdeExcel(boolean cargarDesdeExcel) {
        this.cargarDesdeExcel = cargarDesdeExcel;
    }

    @FXML
    private void regresar(ActionEvent event){
        String ruta = "/fxml/PreRegistroDeCuentas/Main/PreRegistroDeCuentas.fxml";
        String title = "Pre";
        Service.navegacion(ruta, title, event);
    }
}
