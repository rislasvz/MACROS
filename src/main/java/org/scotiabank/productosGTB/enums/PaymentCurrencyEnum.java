package org.scotiabank.productosGTB.enums;

import lombok.Getter;

@Getter
public enum PaymentCurrencyEnum {

    MONEDA_NACIONAL("Moneda Nacional", "00"),
    DOLARES_AMERICANOS("Dolares Americanos", "01");

    private final String name;

    private final String id;

    PaymentCurrencyEnum(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

}
