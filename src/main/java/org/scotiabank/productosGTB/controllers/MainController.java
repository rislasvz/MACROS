package org.scotiabank.productosGTB.controllers;

//import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.scotiabank.productosGTB.business.Service;

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
    private void capturaInformacion(ActionEvent event) {
        String url ="/fxml/captura-informacion-previa.fxml";
        String title = "Sistema Dispersión de Fondos";
        Service service = new Service();
        service.navegacion(url,title, event);
    }

    @FXML
    private void abrirVentanaNuevaInstruccionesPrevias(ActionEvent event) {
        try {
            // 1. Cargar el FXML de la nueva pantalla
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/instrucciones-previas.fxml"));
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