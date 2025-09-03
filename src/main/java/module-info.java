module org.scotiabank.productosGTB.macros {
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.controls;
    requires static lombok;
    requires javafx.base;
    requires org.slf4j;
    //requires MaterialFX;

    opens org.scotiabank.productosGTB to javafx.fxml;
    exports org.scotiabank.productosGTB;
    exports org.scotiabank.productosGTB.controllers;
    opens org.scotiabank.productosGTB.controllers to javafx.fxml;
    opens org.scotiabank.productosGTB.model to javafx.base;
}