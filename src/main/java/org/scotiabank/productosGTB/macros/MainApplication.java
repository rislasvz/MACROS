package org.scotiabank.productosGTB.macros;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/views-fxml/main-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
       Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("PRODUCTO GTB SCOTIBANK MÉXICO");
        Image icon = new Image(MainApplication.class.getResourceAsStream("/assets/Scotiabank_icon_red.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}