package org.scotiabank.productosGTB.business;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Service {

    public static void navegacion (String ruta, String title, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Service.class.getResource(ruta));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            ((Node) event.getSource()).getScene().getWindow().hide();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la nueva ventana.");
        }
    }


    public static void closeAplication() {
        Platform.exit();
        System.exit(0);
    }


    public static void validarNumCliente(String numeroCliente) {
        if (numeroCliente == null || numeroCliente.isBlank()){
            System.out.println();
        }
    }





}
