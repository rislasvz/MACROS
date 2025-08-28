package org.scotiabank.productosGTB.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;


public class LecturaReportesDispersionFondosController {

    @FXML
    private WebView pdfWebView;

    @FXML
    public void initialize() {
        WebEngine engine = pdfWebView.getEngine();

        // PDF por defecto
        String pdfPath = "C:\\Users\\islas\\Documents\\scotiabank\\MACROS\\src\\main\\resources\\files\\demo.pdf";

        // Viewer PDF.js
        String viewerPath = "src/main/resources/pdfjs/web/viewer.html";

        // Usar la clase independiente
        loadPDF(engine, pdfPath, viewerPath);
    }


    public static void loadPDF(WebEngine engine, String pdfPath, String viewerPath) {

        File pdfFile = new File(pdfPath);
        File viewerFile = new File(viewerPath);

        if (!pdfFile.exists()) {
            mostrarError("No se encontró el archivo PDF:\n" + pdfFile.getAbsolutePath());
            return;
        }

        if (!viewerFile.exists()) {
            mostrarError("No se encontró el visor PDF.js:\n" + viewerFile.getAbsolutePath());
            return;
        }

        String pdfUri = pdfFile.toURI().toString();
        String viewerUri = viewerFile.toURI().toString();

        engine.load(viewerUri + "?file=" + pdfUri);
    }

    private static void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error al cargar PDF");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }









}
