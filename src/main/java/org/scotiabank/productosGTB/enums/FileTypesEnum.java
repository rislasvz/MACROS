package org.scotiabank.productosGTB.enums;

public enum FileTypesEnum {

        ALTA("Alta", "1"),
    BAJA("Baja", "2");

    private final String name;
    private final String id;

    FileTypesEnum(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
