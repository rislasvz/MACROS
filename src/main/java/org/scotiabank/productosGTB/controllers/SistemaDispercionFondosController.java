package org.scotiabank.productosGTB.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.scotiabank.productosGTB.model.SistemaDispersionData;

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
    private ComboBox<String> comboBoxFileType;
    @FXML
    private ComboBox<String> comboBoxPaymentConcept;
    @FXML
    private ComboBox<String> comboBoxPaymentCurrency;
    @FXML
    private TableView<SistemaDispersionData> tableViewDispersionFondos;
    private ObservableList<SistemaDispersionData> dataList;
    @FXML
    private TableColumn<SistemaDispersionData, String> formaPago;
    @FXML
    private TableColumn<SistemaDispersionData, String> tipoCuenta;
    //@FXML
    //private TableColumn<SistemaDispersionData, String> bancoReceptor;
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
    private TableColumn<SistemaDispersionData, String> tipoOperación;
    @FXML
    private TableColumn<SistemaDispersionData, String> bancoReceptor;

    private ObservableList<String> bancos = FXCollections.observableArrayList(
            "Scotiabank", "Banamex", "BBVA", "Banorte", "HSBC"
    );


    @FXML
    public void initialize() {
        validaNumeros(textFieldClientNumber);
        validaNumeros(textFieldFileNumberOfTheDay);
        validaNumeros(textFieldCompanyReference);
        validaNumeros(textFieldChargeAccount);
        fillAllComboBox();

        // Inicializa la lista de datos aquí, una sola vez.
        dataList = FXCollections.observableArrayList(
                new SistemaDispersionData("Fila 1, Col 1", "Fila 1, Col 2", "Scotiabank", "Fila 1, Col 4", "Fila 1, Col 5", "Fila 1, Col 6", "Fila 1, Col 7", "Fila 1, Col 8", "Fila 1, Col 9", "Fila 1, Col 10", "Fila 1, Col 11", "Fila 1, Col 12", "Fila 1, Col 13", "Fila 1, Col 14", "Fila 1, Col 15"),
                new SistemaDispersionData("Fila 2, Col 1", "Fila 2, Col 2", "HSBC", "Fila 2, Col 4", "Fila 2, Col 5", "Fila 2, Col 6", "Fila 2, Col 7", "Fila 2, Col 8", "Fila 2, Col 9", "Fila 2, Col 10", "Fila 2, Col 11", "Fila 2, Col 12", "Fila 2, Col 13", "Fila 2, Col 14", "Fila 2, Col 15"),
                new SistemaDispersionData("Fila 3, Col 1", "Fila 3, Col 2", "Banamex", "Fila 3, Col 4", "Fila 3, Col 5", "Fila 3, Col 6", "Fila 3, Col 7", "Fila 3, Col 8", "Fila 3, Col 9", "Fila 3, Col 10", "Fila 3, Col 11", "Fila 3, Col 12", "Fila 3, Col 13", "Fila 3, Col 14", "Fila 3, Col 15")
        );

        // Vincula la lista de datos a la tabla.
        tableViewDispersionFondos.setItems(dataList);

        // Llama a fillTable() para configurar las columnas, pero no para crear datos.
        fillTable();
    }

    public void validaNumeros(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Reemplaza cualquier caracter que no sea un dígito.
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void fillAllComboBox(){
        ObservableList<String> fileTyoes = FXCollections.observableArrayList(
                "Altas",
                "Bajas"
        );
        comboBoxFileType.setItems(fileTyoes);
        comboBoxFileType.getSelectionModel().selectFirst();
        ObservableList<String> paymentConcepts = FXCollections.observableArrayList(
                "Nomina",
                "Pago de Alquiler",
                "Compra de alimentos",
                "Pago de servicios médicos",
                "Matrícula escolar",
                "Transferencia a familiar",
                "Pago de suscripción mensual"
        );
        comboBoxPaymentConcept.setItems(paymentConcepts);
        comboBoxPaymentConcept.getSelectionModel().selectFirst();
        ObservableList<String> paymentCurrencies = FXCollections.observableArrayList(
                "Dolares Americanos",
                "Euro",
                "Libra esterlina",
                "Pesos Mexicanos",
                "Pesos Argentinos",
                "Franco suizo",
                "Dólar canadiense",
                "Yuan Chino",
                "Real Brasileño"
        );
        comboBoxPaymentCurrency.setItems(paymentCurrencies);
        comboBoxPaymentCurrency.getSelectionModel().selectFirst();
    }

    public void fillTable(){
        // Configura las columnas de la tabla. Esta parte es correcta.
        formaPago.setCellValueFactory(new PropertyValueFactory<>("formaPago"));
        tipoCuenta.setCellValueFactory(new PropertyValueFactory<>("tipoCuenta"));
        bancoReceptor.setCellValueFactory(new PropertyValueFactory<>("bancoReceptor"));
        cuenta.setCellValueFactory(new PropertyValueFactory<>("cuenta"));
        // ... el resto de las columnas
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
        tipoOperación.setCellValueFactory(new PropertyValueFactory<>("tipoOperación"));
        tableViewDispersionFondos.setEditable(true);

        formaPago.setCellFactory(TextFieldTableCell.forTableColumn());
        tipoCuenta.setCellFactory(TextFieldTableCell.forTableColumn());
        bancoReceptor.setCellFactory(ComboBoxTableCell.forTableColumn(bancos));

        formaPago.setOnEditCommit(event -> {
            SistemaDispersionData rowData = event.getRowValue();
            rowData.setFormaPago(event.getNewValue());
        });

        tipoCuenta.setOnEditCommit(event -> {
            SistemaDispersionData rowData = event.getRowValue();
            rowData.setTipoCuenta(event.getNewValue());
        });

        bancoReceptor.setOnEditCommit(event -> {
            SistemaDispersionData rowData = event.getRowValue();
            rowData.setBancoReceptor(event.getNewValue());
        });

        // Se eliminó la inicialización de los datos de esta función.
    }

    @FXML
    private void agregarFila(ActionEvent event) {
        // Crea una nueva fila con valores predeterminados (vacíos)
        SistemaDispersionData nuevaFila = new SistemaDispersionData();

        // Agrega la nueva fila a la lista observable de la tabla
        dataList.add(nuevaFila);
    }
}