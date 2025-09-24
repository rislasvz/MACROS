package org.scotiabank.productosGTB.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.scotiabank.productosGTB.business.Service;

import java.io.IOException;

public class CapturaInformacion {

    @FXML
        private void abrirCapturaInformacion(ActionEvent event) {
        String url ="/fxml/sistema-dispersion-fondos.fxml";
        String title = "Sistema Dispersión de Fondos";
        Service service = new Service();
        service.navegacion(url,title, event);
    }

    @FXML
    private void abrirCapturaInformacionPreRegistro(ActionEvent event) {
        String url ="/fxml/PreRegistroCuentas/PreRegistroCuentas.fxml";
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
