package com.example.vm.model.enums;

public enum VisitStatus {
    NOT_STARTED("Not Started"),
    UNDERGOING("Undergoing"),
    CANCELED("Canceled"),
    COMPLETED("Completed");

    private final String value;
    VisitStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
