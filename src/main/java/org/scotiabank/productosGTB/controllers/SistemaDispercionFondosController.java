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
import org.scotiabank.productosGTB.model.SistemaDispersionData;
import org.scotiabank.productosGTB.util.Constants;
import org.scotiabank.productosGTB.util.TooltipManager;
import org.scotiabank.productosGTB.util.Util;

import java.io.*;

public class SistemaDispercionFondosController {

    @FXML
    private TextField textFieldClientNumber;
    @FXML
    private TextField textFieldFileNumberOfTheDay;
    @FXML
    private TextField textFieldChargeAccount;
    @FXML
    private TextField textFieldCompanyReference;
    @FXML
    private TextField textFieldFileType;
    @FXML
    private TextField textFieldPaymentConcept;
    @FXML
    private TextField textFieldPaymentCurrency;
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
    public void initialize() {
        Util.validaNumeros(textFieldClientNumber);
        Util.validaNumeros(textFieldFileNumberOfTheDay);
        Util.validaNumeros(textFieldCompanyReference);
        Util.validaNumeros(textFieldChargeAccount);
        agregaTooltips();

        dataList = FXCollections.observableArrayList(
                new SistemaDispersionData(1, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
        );
        tableViewDispersionFondos.setItems(dataList);
        fillTable();
    }

    public void fillTable(){
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

    public void agregaTooltips(){
        TooltipManager.applyTooltip(textFieldClientNumber, TooltipMessages.CLIENT_NUMBER_TOOLTIP);
        TooltipManager.applyTooltip(textFieldFileNumberOfTheDay, TooltipMessages.FILE_NUMBRER_OF_THE_DAY_TOOLTIP);
        TooltipManager.applyTooltip(textFieldChargeAccount, TooltipMessages.CHARGE_ACCOUNT_TOOLTIP);
        TooltipManager.applyTooltip(textFieldCompanyReference, TooltipMessages.COMPANY_REFERENCE_TOOLTIP);
        TooltipManager.applyTooltip(textFieldFileType, TooltipMessages.FILE_TYPE_TOOLTIP);
        TooltipManager.applyTooltip(textFieldPaymentConcept, TooltipMessages.PAYMENT_CONCEPT_TOOLTIP);
        TooltipManager.applyTooltip(textFieldPaymentCurrency, TooltipMessages.PAYMENT_CURRENCY_TOOLTIP);
    }

    public void exportarDatosAArchivoOptimizada() {
        // 1. Crear un FileChooser para que el usuario elija la ubicación y el nombre del archivo.
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
                writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                        + Constants.TIPO_REGISTRO_HA
                        + Util.rellenarConCeros(textFieldClientNumber.getText(), 5)
                        + Util.rellenarConCeros(textFieldFileNumberOfTheDay.getText(), 2)
                        +"000000000000000000000000000");
                writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                        + Constants.TIPO_REGISTRO_HB
                        + Util.rellenarConCeros(textFieldPaymentCurrency.getText(), 2)
                        + "0000" + textFieldChargeAccount.getText()
                        + textFieldCompanyReference.getText() + "000");


                // 2. Obtener los datos de la tabla
                for (SistemaDispersionData data : dataList) {
                    // 3. Formatear la información de cada fila y escribirla en el archivo
                    writer.println(Constants.ARCHIVO_MOVIMIENTOS_ENTRADA
                        + Constants.TIPO_REGISTRO_DA + data.getFormaPago() + Util.rellenarConCeros(textFieldPaymentCurrency.getText(), 2));
                }
                // 4. Mostrar una alerta de éxito
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
    }


}