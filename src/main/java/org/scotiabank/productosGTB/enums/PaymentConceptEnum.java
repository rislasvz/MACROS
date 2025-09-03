package org.scotiabank.productosGTB.enums;

import lombok.Getter;

@Getter
public enum PaymentConceptEnum {

    NOMINA("Nomina", "01"),
    PENSIONES("Pensiones", "02"),
    OTRAS_TRANSFERENCIAS("Otras Transferencias", "03");

    private final String name;
    private final String id;

    PaymentConceptEnum(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
