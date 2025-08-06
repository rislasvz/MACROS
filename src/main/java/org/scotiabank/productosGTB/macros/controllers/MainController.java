package org.scotiabank.productosGTB.macros.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // Anotación para inyectar el Label desde el FXML
    @FXML
    private Label labelFecha;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mostrarFechaActual();
    }

    private void mostrarFechaActual() {
        LocalDate fechaActual = LocalDate.now();
        Locale locale = new Locale("es", "ES");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'del' yyyy", locale);
        String fechaFormateada = fechaActual.format(formatter);
        labelFecha.setText(fechaFormateada);
    }
}