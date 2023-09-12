package com.example.vm.model.enums;

public enum VisitTypeBase {
    QUESTION,
    PAYMENT;

    public static VisitTypeBase fromInt(int x) {
        return switch (x) {
            case 0 -> QUESTION;
            case 1 -> PAYMENT;
            default -> null;
        };
    }
}
