package com.example.vm.model.enums;

public enum PaymentType {
    CASH,
    CHECK,
    VISA;

    public static PaymentType valueOf(int x) {
        return switch (x) {
            case 0 -> CASH;
            case 1 -> CHECK;
            case 2 -> VISA;
            default -> null;
        };
    }
}
