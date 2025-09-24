package org.scotiabank.productosGTB.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class TextFieldValidator {

    public static void validarNumerosYMaxLength(TextField textField, int maxLength) {
        String texto = textField.getText();
        // Validar que solo contenga números
        if (!texto.matches("\\d*")) {
            mostrarAlerta("Solo se permiten números.");
            Platform.runLater(textField::clear);
            return;
        }
        // Validar longitud máxima
        if (texto.length() > maxLength) {
            mostrarAlerta("Máximo " + maxLength + " caracteres permitidos.");
            Platform.runLater(textField::clear);
        }
    }

    public static void validarMinLength(TextField textField, int minLength) {
        String texto = textField.getText();
        if (texto.length() < minLength) {
            mostrarAlerta("Debe ingresar al menos " + minLength + " caracteres.");
            Platform.runLater(textField::clear);
        }
    }

    private static void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validación de campo");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
