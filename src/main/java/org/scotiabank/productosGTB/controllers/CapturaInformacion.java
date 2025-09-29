package org.scotiabank.productosGTB.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.scotiabank.productosGTB.business.Service;

import java.io.IOException;

public class CapturaInformacion {

    @FXML
        private void abrirCapturaInformacionSinExcel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Service.class.getResource("/fxml/sistema-dispersion-fondos.fxml"));
            Parent root = fxmlLoader.load();
            SistemaDispercionFondosController controlador = fxmlLoader.getController();
            controlador.setCargarDesdeExcel(false);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Sistema Dispersión de Fondos");
            stage.setScene(scene);
            ((Node) event.getSource()).getScene().getWindow().hide();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la nueva ventana.");
        }
    }

    @FXML
    private void abrirCapturaInformacionConExcel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Service.class.getResource("/fxml/sistema-dispersion-fondos.fxml"));
            Parent root = fxmlLoader.load();
            SistemaDispercionFondosController controlador = fxmlLoader.getController();
            controlador.setCargarDesdeExcel(true);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Sistema Dispersión de Fondos");
            stage.setScene(scene);
            ((Node) event.getSource()).getScene().getWindow().hide();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la nueva ventana.");
        }
    }

    @FXML
    private void abrirCapturaInformacionPreRegistro(ActionEvent event) {
        String url = "/fxml/PreRegistroDeCuentas/PreRegistroCuentas.fxml";
        String title = "Pre-Registro Cuentas";
        Service service = new Service();
        service.navegacion(url,title, event);
    }



    @FXML
    private void abrirOpcionesDispercionFondos(ActionEvent event){
        String url ="/fxml/preview.fxml";
        String title = "Sistema Dispersión de Fondos";
        Service service = new Service();
        service.navegacion(url,title, event);
    }

    @FXML
    private void abrirResultados(ActionEvent event) {
        String url ="/fxml/dispersionFondosRespuesta.fxml";
        String title = "Dispersion Fondos Respuesta";
        Service service = new Service();
        service.navegacion(url,title, event);
    }





}
