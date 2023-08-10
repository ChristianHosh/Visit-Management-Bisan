package com.example.vm.controller.error.exception;

public class UserNotFoundException extends RuntimeException {

    public static final String CUSTOMER_NOT_FOUND = "Invalid ID : Customer Not Found";
    public static final String CUSTOMER_NOT_ASSIGNED = "Invalid ID : Customer Not Found In Assignment";
    public static final String CONTACT_NOT_FOUND = "Invalid ID : Contact Not Found";
    public static final String USER_NOT_FOUND = "Invalid ID : User Not Found";
    public static final String DEFINITION_NOT_FOUND = "Invalid ID : Visit Definition Not Found";
    public static final String ASSIGNMENT_NOT_FOUND = "Invalid ID : Visit Assignment Not Found";
    public static final String TYPE_NOT_FOUND = "Invalid ID : Visit Type Not Found";


    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
