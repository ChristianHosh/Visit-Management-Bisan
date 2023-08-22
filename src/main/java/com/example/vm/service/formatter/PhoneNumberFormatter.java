package com.example.vm.service.formatter;

public class PhoneNumberFormatter {
    public static String formatPhone(String phoneNumber) {
        return phoneNumber.replaceAll("[(). -]", "");
    }

}
