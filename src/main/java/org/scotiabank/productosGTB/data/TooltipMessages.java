package org.scotiabank.productosGTB.data;

public final class TooltipMessages  {

    // Evita la instanciación de la clase
    private TooltipMessages() {}

    public static final String CLIENT_NUMBER_TOOLTIP = "REQUERIDO\n" +
            "Solo números\n" +
            "Minimo 1 dígito\n" +
            "Maximo 12 digitos";

    public static final String FILE_TYPE_TOOLTIP = "REQUERIDO\n" +
            "Solo números\n" +
            "Minimo y máximo 1 dígito\n" +
            "Valores aceptados: 1 y 2";

    public static final String FILE_NUMBRER_OF_THE_DAY_TOOLTIP = "REQUERIDO\n" +
            "Solo números\n" +
            "Minimo 1 dígito\n" +
            "Minimo 2 dígito\n" +
            "Rango del 1 al 99";

    public static final String CHARGE_ACCOUNT_TOOLTIP = "REQUERIDO\n" +
            "Solo números\n" +
            "Minimo y máximo 11 dígitos";

    public static final String COMPANY_REFERENCE_TOOLTIP = "REQUERIDO\n" +
            "Solo números\n" +
            "Minimo y máximo 10 dígitos";

    public static final String PAYMENT_CONCEPT_TOOLTIP = "REQUERIDO\n" +
            "Solo números\n" +
            "Minimo y máximo 1 dígito\n" +
            "Valores aceptados: 1, 2 y 3";

    public static final String PAYMENT_CURRENCY_TOOLTIP = "REQUERIDO\n" +
            "Solo números\n" +
            "Minimo y máximo 2 dígito\n" +
            "Valores aceptados: 00 y 01";

}
