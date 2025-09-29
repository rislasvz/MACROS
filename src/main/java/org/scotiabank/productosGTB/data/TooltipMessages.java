package org.scotiabank.productosGTB.data;

public final class TooltipMessages  {

    // Evita la instanciación de la clase
    private TooltipMessages() {}

    public static final String CLIENT_NUMBER_TOOLTIP = "REQUERIDO\n" +
            "Solo números\n" +
            "Minimo 1 dígito\n" +
            "Maximo 12 digitos";

    public static final String FILE_NUMBRER_OF_THE_DAY_TOOLTIP = "REQUERIDO\n" +
            "Solo números\n" +
            "Minimo 1 dígito\n" +
            "Maximo 2 dígitos\n" +
            "Rango del 1 al 99";

    public static final String CHARGE_ACCOUNT_TOOLTIP = "REQUERIDO\n" +
            "Solo números\n" +
            "Minimo y máximo 11 dígitos";

    public static final String COMPANY_REFERENCE_TOOLTIP = "REQUERIDO\n" +
            "Solo números\n" +
            "Minimo y máximo 10 dígitos";

    public static final String CONTRACT_NUMBER_TOOLTIP = "REQUERIDO\n" +
            "Solo números\n" +
            "Minimo 1 dígito\n" +
            "Maximo 7 dígitos";

}
