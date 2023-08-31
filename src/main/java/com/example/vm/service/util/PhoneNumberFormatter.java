package com.example.vm.service.util;

public class PhoneNumberFormatter {
    public static String formatPhone(String phoneNumber) {
        return phoneNumber.replaceAll("[(). -]", "");
    }

}
