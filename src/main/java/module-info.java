module org.scotiabank.productosGTB.macros {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens org.scotiabank.productosGTB.macros to javafx.fxml;
    exports org.scotiabank.productosGTB.macros;
    exports org.scotiabank.productosGTB.macros.controllers;
    opens org.scotiabank.productosGTB.macros.controllers to javafx.fxml;
}