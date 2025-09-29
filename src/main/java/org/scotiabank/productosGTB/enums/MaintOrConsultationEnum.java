package org.scotiabank.productosGTB.enums;

import lombok.Getter;

@Getter
public enum MaintOrConsultationEnum {

    MANTENIMIENTO("Mantenimiento", "M"),
    CONSULTA("Consulta", "C");

    private final String name;
    private final String id;

    MaintOrConsultationEnum(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

}
