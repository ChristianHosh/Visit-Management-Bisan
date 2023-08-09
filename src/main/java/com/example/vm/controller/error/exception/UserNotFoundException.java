package com.example.vm.controller.error.exception;

public class UserNotFoundException extends RuntimeException {

    public static final String CUSTOMER_NOT_FOUND = "Invalid ID : Customer Not Found";
    public static final String ASSIGNMENT_NOT_FOUND = "Invalid ID : Assignment Not Found";

    public static final String CONTACT_NOT_FOUND = "Invalid ID : Contact Not Found";
    public static final String USER_NOT_FOUND = "Invalid ID : User Not Found";
    public static final String DEFINITION_NOT_FOUND = "Invalid ID : Definition Not Found";


    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }

    public UserNotFoundException(String errorMessage){

    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
