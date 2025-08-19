package org.scotiabank.productosGTB.macros.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CapturaInformacionPreviaController {

    @FXML
        private void abrirCapturaInformacion(ActionEvent event) {
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
