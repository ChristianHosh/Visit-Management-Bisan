package com.example.vm.service.formatter;

public class PhoneNumberFormatter {
    public static String formatPhone(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }

        String normalizedNumber = phoneNumber.replaceAll("[. -]", "");

        return normalizedNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
    }

}
