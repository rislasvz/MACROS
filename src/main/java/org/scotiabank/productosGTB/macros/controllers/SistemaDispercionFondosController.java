package org.scotiabank.productosGTB.macros.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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
    public void initialize() {
        validaNumeros(textFieldClientNumber);
        validaNumeros(textFieldFileNumberOfTheDay);
        validaNumeros(textFieldCompanyReference);
        validaNumeros(textFieldChargeAccount);
        fillAllComboBox();


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



}
