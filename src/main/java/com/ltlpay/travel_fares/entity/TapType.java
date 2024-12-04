package com.ltlpay.travel_fares.entity;

public enum TapType {
    ON("ON"),
    OFF("OFF");
    private final String description;

    TapType(String description) {
        this.description = description;
    }

    public static TapType fromString(String value) {
        for (TapType tapType : TapType.values()) {
            if (tapType.name().equalsIgnoreCase(value)) {
                return tapType;
            }
        }
        throw new IllegalArgumentException("Unknown TapType: " + value);
    }

    public String getDescription() {
        return description;
    }


}
