package org.scotiabank.productosGTB.util;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.function.UnaryOperator;

import java.util.function.BiConsumer;

public class Util {

    public static void validaNumeros(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public static <S> void configurarEdicionColumnaString(TableColumn<S, String> columna, BiConsumer<S, String> setter) {
        columna.setCellFactory(TextFieldTableCell.forTableColumn());
        columna.setOnEditCommit(event -> {
            S rowData = event.getRowValue();
            String newValue = event.getNewValue();
            setter.accept(rowData, newValue);
        });
    }

    public static <S> void configurarEdicionColumnaInteger(TableColumn<S, Integer> columna, BiConsumer<S, Integer> setter) {
        columna.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        columna.setOnEditCommit(event -> {
            S rowData = event.getRowValue();
            Integer newValue = event.getNewValue();
            setter.accept(rowData, newValue);
        });
    }

    public static <S> void configurarEdicionColumnaLlenaString(TableColumn<S, String> columna, BiConsumer<S, String> setter) {
        columna.setOnEditCommit(event -> {
            S rowData = event.getRowValue();
            String newValue = event.getNewValue();
            setter.accept(rowData, newValue);
        });
    }

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

                    // Validar la longitud
                    if (newText.length() > maxLength) {
                        // Mostrar una alerta si se excede la longitud máxima
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Advertencia de Longitud");
                            alert.setHeaderText(null);
                            alert.setContentText("La longitud máxima permitida es de " + maxLength + " d\u00edgitos.");
                            alert.show();
                        });
                        return null; // Denegar el cambio
                    }

                    // Validar el formato numérico y decimal
                    if (newText.matches("\\d*\\.?\\d*")) {
                        return change;
                    }

                    return null; // Denegar el cambio
                };

                textField.setTextFormatter(new TextFormatter<>(filter));

                textField.setOnAction(event -> commitEdit(textField.getText()));
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        // Validar la longitud mínima al perder el foco
                        Platform.runLater(() -> {
                            if (textField.getText().length() < minLength) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error de Validaci\u00f3n");
                                alert.setHeaderText(null);
                                alert.setContentText("La longitud m\u00ednima requerida es de " + minLength + " d\u00edgitos.");
                                alert.showAndWait();
                                cancelEdit();
                            } else {
                                commitEdit(textField.getText());
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

                    // Validar la longitud
                    if (newText.length() > maxLength) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Advertencia de Longitud");
                            alert.setHeaderText(null);
                            alert.setContentText("La longitud máxima permitida es de " + maxLength + " d\u00edgitos.");
                            alert.show();
                        });
                        return null;
                    }

                    // Validar el formato numérico y decimal
                    if (newText.matches("\\d*\\.?\\d{0,2}")) {
                        return change;
                    }

                    return null;
                };

                textField.setTextFormatter(new TextFormatter<>(filter));

                textField.setOnAction(event -> commitEdit(textField.getText()));
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        // Validar la longitud mínima al perder el foco
                        Platform.runLater(() -> {
                            if (textField.getText().length() < minLength) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error de Validaci\u00f3n");
                                alert.setHeaderText(null);
                                alert.setContentText("La longitud m\u00ednima requerida es de " + minLength + " d\u00edgitos.");
                                alert.showAndWait();
                                cancelEdit();
                            } else {
                                commitEdit(textField.getText());
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
                if (empty) {
                    if (!getStyleClass().contains("error-cell")) {
                        getStyleClass().add("error-cell");
                    }
                    setText(null);
                    setGraphic(null);
                } else {
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

                    // Validar la longitud
                    if (newText.length() > maxLength) {
                        // Mostrar una alerta si se excede la longitud máxima
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Advertencia de Longitud");
                            alert.setHeaderText(null);
                            alert.setContentText("La longitud máxima permitida es de " + maxLength + " d\u00edgitos.");
                            alert.show();
                        });
                        return null; // Denegar el cambio
                    }

                    // Validar el formato numérico y decimal
                    if (newText.matches("[\\w\\sáéíóúÁÉÍÓÚñÑ]*") && newText.length() <= maxLength) {
                        return change;
                    }

                    return null; // Denegar el cambio
                };

                textField.setTextFormatter(new TextFormatter<>(filter));

                textField.setOnAction(event -> commitEdit(textField.getText()));
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        // Validar la longitud mínima al perder el foco
                        Platform.runLater(() -> {
                            if (textField.getText().length() < minLength) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error de Validaci\u00f3n");
                                alert.setHeaderText(null);
                                alert.setContentText("La longitud m\u00ednima requerida es de " + minLength + " d\u00edgitos.");
                                alert.showAndWait();
                                cancelEdit();
                            } else {
                                commitEdit(textField.getText());
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
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
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

                    // Validar la longitud
                    if (newText.length() > maxLength) {
                        // Mostrar una alerta si se excede la longitud máxima
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Advertencia de Longitud");
                            alert.setHeaderText(null);
                            alert.setContentText("La longitud máxima permitida es de " + maxLength + " d\u00edgitos.");
                            alert.show();
                        });
                        return null; // Denegar el cambio
                    }

                    // Validar el formato numérico y decimal
                    if (newText.matches("^[\\w\\.@+-]*$") && newText.length() <= maxLength) {
                        return change;
                    }

                    return null; // Denegar el cambio
                };

                textField.setTextFormatter(new TextFormatter<>(filter));

                textField.setOnAction(event -> commitEdit(textField.getText()));
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        // Validar la longitud mínima al perder el foco
                        Platform.runLater(() -> {
                            if (textField.getText().length() < minLength) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error de Validaci\u00f3n");
                                alert.setHeaderText(null);
                                alert.setContentText("La longitud m\u00ednima requerida es de " + minLength + " d\u00edgitos.");
                                alert.showAndWait();
                                cancelEdit();
                            } else {
                                commitEdit(textField.getText());
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

}
