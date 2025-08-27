package org.scotiabank.productosGTB.macros.util;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class TooltipManager {

    /**
     * Aplica un Tooltip a un TextField con un mensaje específico.
     * @param textField El TextField al que se le aplicará el Tooltip.
     * @param message El mensaje del Tooltip.
     */
    public static void applyTooltip(TextField textField, String message) {
        Tooltip tooltip = new Tooltip(message);
        Tooltip.install(textField, tooltip);
    }
}
