package org.scotiabank.productosGTB.util;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.util.Callback;

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

                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null && !newValue.isEmpty() && !allowedValues.contains(newValue)) {
                        Platform.runLater(() -> {
                            mostrarAlerta("El valor para 'Forma de pago' solo puede ser 1, 2, 3, 4 o 10.");
                            textField.clear(); // Borrar contenido inválido
                        });
                    }
                });

                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        String value = textField.getText();
                        if (validate(value)) {
                            commitEdit(value);
                        } else {
                            mostrarAlerta("El valor para 'Forma de pago' solo puede ser 1, 2, 3, 4 o 10.");
                            cancelEdit();
                        }
                    }
                });
            }

            private boolean validate(String value) {
                return allowedValues.contains(value);
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

    private static void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
