package org.scotiabank.productosGTB.util;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.scotiabank.productosGTB.data.ErrorData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class Util {

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> createNumericCellFactory(int minLength, int maxLength) {
        return column -> new TableCell<S, String>() {
            private TextField textField;

            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    super.startEdit();
                    if (textField == null) {
                        createTextField(minLength, maxLength);
                    }
                    textField.setText(getString());
                    setGraphic(textField);
                    setText(null);
                    textField.selectAll();
                    textField.requestFocus();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getString());
                setGraphic(null);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.trim().isEmpty()) {
                    setText(null);
                    setGraphic(null);
                    if (!getStyleClass().contains("error-cell")) {
                        getStyleClass().add("error-cell");
                    }
                } else {
                    getStyleClass().remove("error-cell");
                    if (isEditing()) {
                        if (textField != null) {
                            textField.setText(getString());
                        }
                        setGraphic(textField);
                        setText(null);
                    } else {
                        setText(getString());
                        setGraphic(null);
                    }
                }
            }

            private void createTextField(int minLength, int maxLength) {
                textField = new TextField();

                UnaryOperator<TextFormatter.Change> filter = change -> {
                    String newText = change.getControlNewText();

                    // Validar solo números enteros
                    if (!newText.matches("\\d*")) {
                        Platform.runLater(() -> {
                            mostrarAlerta("Solo se permiten números enteros.");
                            textField.clear(); // Borrar contenido
                        });
                        return null;
                    }

                    // Validar longitud máxima
                    if (newText.length() > maxLength) {
                        Platform.runLater(() -> {
                            mostrarAlerta("La longitud máxima permitida es de " + maxLength + " dígitos.");
                            textField.clear(); // Opcional: borrar contenido si lo deseas
                        });
                        return null;
                    }


                    return change;
                };

                textField.setTextFormatter(new TextFormatter<>(filter));

                textField.setOnAction(event -> commitEdit(textField.getText()));
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        Platform.runLater(() -> {
                            String value = textField.getText();
                            if (value.length() < minLength) {
                                if (!value.isEmpty()) {
                                    mostrarAlerta("La longitud mínima requerida es de " + minLength + " dígitos.");
                                }
                                cancelEdit();
                            } else {
                                commitEdit(value);
                            }
                        });
                    }
                });
            }

            private String getString() {
                return getItem() == null ? "" : getItem().toString();
            }
        };
    }

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> createDecimalCellFactory(int minLength, int maxLength) {
        return column -> new TableCell<S, String>() {
            private TextField textField;

            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    super.startEdit();
                    if (textField == null) {
                        createTextField(minLength, maxLength);
                    }
                    textField.setText(getString());
                    setGraphic(textField);
                    setText(null);
                    textField.selectAll();
                    textField.requestFocus();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getString());
                setGraphic(null);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.trim().isEmpty()) {
                    setText(null);
                    setGraphic(null);
                    if (!getStyleClass().contains("error-cell")) {
                        getStyleClass().add("error-cell");
                    }
                } else {
                    getStyleClass().remove("error-cell");
                    if (isEditing()) {
                        if (textField != null) {
                            textField.setText(getString());
                        }
                        setGraphic(textField);
                        setText(null);
                    } else {
                        setText(getString());
                        setGraphic(null);
                    }
                }
            }

            private void createTextField(int minLength, int maxLength) {
                textField = new TextField();

                UnaryOperator<TextFormatter.Change> filter = change -> {
                    String newText = change.getControlNewText();

                    // Validar caracteres permitidos: hasta dos decimales
                    if (!newText.matches("\\d*(\\.\\d{0,2})?")) {
                        Platform.runLater(() -> {
                            mostrarAlerta("La longitud máxima permitida es de " + maxLength + " dígitos.");
                            textField.clear(); // Borrar contenido
                        });
                        return null;
                    }

                    // Validar longitud máxima
                    if (newText.length() > maxLength) {
                        Platform.runLater(() -> {
                            mostrarAlerta("Solo se permiten números con hasta dos decimales.");
                            textField.clear(); // Opcional: borrar contenido si lo deseas
                        });
                        return null;
                    }
                    return change;
                };

                textField.setTextFormatter(new TextFormatter<>(filter));

                textField.setOnAction(event -> commitEdit(formatDecimal(textField.getText())));
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        Platform.runLater(() -> {
                            String value = textField.getText();
                            if (value.length() < minLength) {
                                if (!value.isEmpty()) {
                                    mostrarAlerta("La longitud mínima requerida es de " + minLength + " dígitos.");
                                }
                                cancelEdit();
                            } else {
                                commitEdit(formatDecimal(value));
                            }
                        });
                    }
                });
            }

            private String getString() {
                return getItem() == null ? "" : getItem().toString();
            }

            private String formatDecimal(String value) {
                try {
                    double number = Double.parseDouble(value);
                    return String.format("%.2f", number);
                } catch (NumberFormatException e) {
                    return value;
                }
            }
        };
    }

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> createStringWithoutSymbolsCellFactory(int minLength, int maxLength) {
        return column -> new TableCell<S, String>() {
            private TextField textField;

            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    super.startEdit();
                    if (textField == null) {
                        createTextField(minLength, maxLength);
                    }
                    textField.setText(getString());
                    setGraphic(textField);
                    setText(null);
                    textField.selectAll();
                    textField.requestFocus();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getString());
                setGraphic(null);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.trim().isEmpty()) {
                    setText(null);
                    setGraphic(null);
                    if (!getStyleClass().contains("error-cell")) {
                        getStyleClass().add("error-cell");
                    }
                } else {
                    getStyleClass().remove("error-cell");
                    if (isEditing()) {
                        if (textField != null) {
                            textField.setText(getString());
                        }
                        setGraphic(textField);
                        setText(null);
                    } else {
                        setText(getString());
                        setGraphic(null);
                    }
                }
            }

            private void createTextField(int minLength, int maxLength) {
                textField = new TextField();

                UnaryOperator<TextFormatter.Change> filter = change -> {
                    String newText = change.getControlNewText();

                    // Validar longitud máxima
                    if (newText.length() > maxLength) {
                        Platform.runLater(() -> {
                            mostrarAlerta("La longitud máxima permitida es de " + maxLength + " caracteres.");
                            textField.clear();
                        });
                        return null;
                    }

                    // Validar solo letras, números y espacios (sin acentos ni Ñ)
                    if (!newText.matches("[a-zA-Z0-9 ]*")) {
                        Platform.runLater(() -> {
                            mostrarAlerta("Solo se permiten letras sin acentos, números y espacios.");
                            textField.clear();
                        });
                        return null;
                    }

                    return change;
                };

                textField.setTextFormatter(new TextFormatter<>(filter));

                textField.setOnAction(event -> commitEdit(textField.getText()));
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        Platform.runLater(() -> {
                            String value = textField.getText();
                            if (value.length() < minLength) {
                                if (!value.isEmpty()) {
                                    mostrarAlerta("La longitud mínima requerida es de " + minLength + " caracteres.");
                                }
                                cancelEdit();
                            } else {
                                commitEdit(value);
                            }
                        });
                    }
                });
            }

            private String getString() {
                return getItem() == null ? "" : getItem().toString();
            }
        };
    }

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> createStringEmailCellFactory(int minLength, int maxLength) {
        return column -> new TableCell<S, String>() {
            private TextField textField;

            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    super.startEdit();
                    if (textField == null) {
                        createTextField(minLength, maxLength);
                    }
                    textField.setText(getString());
                    setGraphic(textField);
                    setText(null);
                    textField.selectAll();
                    textField.requestFocus();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getString());
                setGraphic(null);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.trim().isEmpty()) {
                    setText(null);
                    setGraphic(null);
                    if (!getStyleClass().contains("error-cell")) {
                        getStyleClass().add("error-cell");
                    }
                } else {
                    getStyleClass().remove("error-cell");
                    if (isEditing()) {
                        if (textField != null) {
                            textField.setText(getString());
                        }
                        setGraphic(textField);
                        setText(null);
                    } else {
                        setText(getString());
                        setGraphic(null);
                    }
                }
            }

            private void createTextField(int minLength, int maxLength) {
                textField = new TextField();

                UnaryOperator<TextFormatter.Change> filter = change -> {
                    String newText = change.getControlNewText();

                    // Validar longitud máxima
                    if (newText.length() > maxLength) {
                        Platform.runLater(() -> {
                            mostrarAlerta("La longitud máxima permitida es de " + maxLength + " caracteres.");
                            textField.clear();
                        });
                        return null;
                    }

                    // Validar caracteres permitidos en email
                    if (!newText.matches("[a-zA-Z0-9@._+-]*")) {
                        Platform.runLater(() -> {
                            mostrarAlerta("Solo se permiten caracteres válidos en una dirección de correo electrónico.");
                            textField.clear();
                        });
                        return null;
                    }

                    return change;
                };

                textField.setTextFormatter(new TextFormatter<>(filter));

                textField.setOnAction(event -> commitEdit(textField.getText()));
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        Platform.runLater(() -> {
                            String value = textField.getText();
                            if (value.length() < minLength) {
                                if (!value.isEmpty()) {
                                    mostrarAlerta("La longitud mínima requerida es de " + minLength + " caracteres.");
                                }
                                cancelEdit();
                            } else {
                                commitEdit(value);
                            }
                        });
                    }
                });
            }

            private String getString() {
                return getItem() == null ? "" : getItem().toString();
            }
        };
    }

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> createPersonalizedCellFactory(List<String> allowedValues) {
        return column -> new TableCell<S, String>() {
            private TextField textField;

            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    super.startEdit();
                    if (textField == null) {
                        createTextField();
                    }
                    textField.setText(getString());
                    setGraphic(textField);
                    setText(null);
                    Platform.runLater(() -> textField.requestFocus());
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getString());
                setGraphic(null);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.trim().isEmpty()) {
                    setText(null);
                    setGraphic(null);
                    if (!getStyleClass().contains("error-cell")) {
                        getStyleClass().add("error-cell");
                    }
                } else {
                    getStyleClass().remove("error-cell");
                    setText(item);
                    setGraphic(null);
                }
            }

            private void createTextField() {
                textField = new TextField(getString());

                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        String value = textField.getText();
                        if (validate(value)) {
                            commitEdit(value);
                        } else {
                            mostrarAlerta("El valor para 'Forma de pago' solo puede ser: " + String.join(", ", allowedValues));
                            cancelEdit();
                        }
                    }
                });
            }

            private boolean validate(String value) {
                return allowedValues.contains(value);
            }

            private String getString() {
                return getItem() == null ? "" : getItem();
            }

            private void mostrarAlerta(String mensaje) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                alert.setTitle("Valor inválido");
                alert.setHeaderText(null);
                alert.setContentText(mensaje);
                alert.showAndWait();
            }
        };
    }



    public static String rellenarConCerosIzquierda(String cadena, int longitudTotal) {
        if (cadena.length() >= longitudTotal) {
            return cadena;
        }
        int cantidadDeCeros = longitudTotal - cadena.length();
        StringBuilder ceros = new StringBuilder();
        for (int i = 0; i < cantidadDeCeros; i++) {
            ceros.append("0");
        }
        return ceros.toString() + cadena;
    }

    public static String rellenarConEspaciosDerecha(String cadena, int longitudTotal) {
        if (cadena.length() >= longitudTotal) {
            return cadena;
        }
        int cantidadDeEspacios = longitudTotal - cadena.length();
        StringBuilder espacios = new StringBuilder();
        for (int i = 0; i < cantidadDeEspacios; i++) {
            espacios.append(" ");
        }
        return cadena + espacios.toString(); // Espacios a la derecha
    }

    public static void limitarFechas(DatePicker miDatePicker) {
        final LocalDate hoy = LocalDate.now();
        miDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(hoy)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #A39E9E;"); // Opcional: para visualmente indicar que están deshabilitadas
                }
            }
        });
    }

    public static boolean isValidNumeric(String value) {
        if (value == null || value.trim().isEmpty()) return true;
        return value.matches("\\d*\\.?\\d*");
    }

    public static boolean isValidDecimal(String value) {
        if (value == null || value.trim().isEmpty()) return true;
        return value.matches("\\d*\\.?\\d{0,2}");
    }

    public static boolean isValidStringWithoutSymbols(String value) {
        if (value == null || value.trim().isEmpty()) return true;
        return value.matches("[a-zA-Z0-9 ]*");
    }

    public static boolean isValidEmail(String value) {
        if (value == null || value.trim().isEmpty()) return true;
        return value.matches("[a-zA-Z0-9@._+-]*");
    }

    public static boolean isValidAllowedValue(String value, List<String> allowedValues) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return allowedValues.contains(value);
    }

    public static void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Dentro de SistemaDispercionFondosController.java

    public static void exportarErroresAExcel(List<ErrorData> errores) {
        // 1. Crear el libro de trabajo (Workbook)
        Workbook workbook = new XSSFWorkbook(); // O HSSFWorkbook para .xls
        Sheet sheet = workbook.createSheet("Reporte de Errores");

        // 2. Crear la fila de encabezados
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Fila Original");
        headerRow.createCell(1).setCellValue("Columna");
        headerRow.createCell(2).setCellValue("Valor Ingresado");
        headerRow.createCell(3).setCellValue("Mensaje de Error");

        // 3. Llenar los datos de error
        int rowNum = 1;
        for (ErrorData error : errores) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(error.fila);
            row.createCell(1).setCellValue(error.columna);
            row.createCell(2).setCellValue(error.valor);
            row.createCell(3).setCellValue(error.mensaje);
        }

        // Autoajustar el ancho de las columnas (opcional pero recomendado)
        for(int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        // 4. Mostrar el diálogo para guardar el archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte de Errores");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));
        fileChooser.setInitialFileName("Reporte_Errores_Importacion_" + System.currentTimeMillis() + ".xlsx");


        File file = fileChooser.showSaveDialog(new Stage()); // Usar una Stage si no tienes acceso a la principal

        // 5. Escribir el archivo
        if (file != null) {
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Reporte Generado");
                alert.setHeaderText(null);
                alert.setContentText("El archivo de errores se ha generado exitosamente en:\n" + file.getAbsolutePath());
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                // Manejar error de escritura
            }
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
