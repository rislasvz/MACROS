module org.scotiabank.productosGTB.macros {
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.controls;
    //requires MaterialFX;
                            
    opens org.scotiabank.productosGTB.macros to javafx.fxml;
    exports org.scotiabank.productosGTB.macros;
    exports org.scotiabank.productosGTB.macros.controllers;
    opens org.scotiabank.productosGTB.macros.controllers to javafx.fxml;
    opens org.scotiabank.productosGTB.macros.model to javafx.base;
}