package com.ltlpay.travel_fares.entity;

public enum TripType {
    COMPLETED("COMPLETED"),
    INCOMPLETE("INCOMPLETE"),
    CANCELLED("CANCELLED");
    private String value;
    private TripType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public static TripType fromString(String value) {
        for (TripType tripType : TripType.values()) {
            if (tripType.name().equalsIgnoreCase(value)) {
                return tripType;
            }
        }
        throw new IllegalArgumentException("Unknown TripType: " + value);
    }
}
