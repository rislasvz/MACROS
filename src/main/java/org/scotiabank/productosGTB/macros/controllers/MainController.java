package org.scotiabank.productosGTB.macros.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
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

    @FXML
    private void abrirVentanaNueva(ActionEvent event) {
        try {
            // 1. Cargar el FXML de la nueva pantalla
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views-fxml/sistema-dispersion-fondos.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // 2. Crear un nuevo Stage (ventana)
            Stage stage = new Stage();

            // 3. Configurar la nueva ventana
            stage.setTitle("Sistema Dispersión de Fondos");
            stage.setScene(scene);

            // Opcional: Si quieres que la ventana actual se cierre
            ((Node) event.getSource()).getScene().getWindow().hide();

            // 4. Mostrar la nueva ventana
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la nueva ventana.");
        }
    }
}