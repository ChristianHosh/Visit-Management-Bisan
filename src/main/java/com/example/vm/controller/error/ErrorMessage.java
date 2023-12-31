package com.example.vm.controller.error;

public enum ErrorMessage {

    // NOT FOUND MESSAGES
    LOCATION_NOT_FOUND("Entity not found: Location not found"),
    CITY_NOT_FOUND("Entity not found: City not found"),
    CONTACT_NOT_FOUND("Entity not found: Contact not found"),
    CUSTOMER_NOT_FOUND("Entity not found: Customer not Found"),
    USER_NOT_FOUND("Entity not found: User not found"),
    ASSIGNMENT_NOT_FOUND("Entity not found: Visit Assignment not found"),
    DEFINITION_NOT_FOUND("Entity not found: Visit Definition not found"),
    TYPE_NOT_FOUND("Entity not found: Visit Type not found"),
    FORM_NOT_FOUND("Entity not found: Visit Form not found"),
    CUSTOMER_NOT_ASSIGNED("Entity not found: Customer is not assigned"),
    SURVEY_NOT_FOUND("Not found: Survey Template not found"),
    PASSWORD_RESET_NOT_FOUND("Not found: password reset request not found"),

    // NOT ENABLED MESSAGES
    CITY_NOT_ENABLED("Entity not enabled: City not enabled"),
    CONTACT_NOT_ENABLED("Entity not enabled: Contact not enabled"),
    CUSTOMER_NOT_ENABLED("Entity not enabled: Customer not enabled"),
    USER_NOT_ENABLED("Entity not enabled: User not enabled"),
    ASSIGNMENT_NOT_ENABLED("Entity not enabled: Visit Assignment not enabled"),
    DEFINITION_NOT_ENABLED("Entity not enabled: Visit Definition not enabled"),
    TYPE_NOT_ENABLED("Entity not enabled: Visit Type not enabled"),
    FORM_NOT_ENABLED("Entity not enabled: Visit Form not enabled"),


    // BAD REQUEST MESSAGES
    PASSWORDS_DO_NOT_MATCH("Bad request: Passwords should match"),
    INVALID_ASSIGNMENT_CONTACT_TYPES("Bad request: No Contacts of this type are available"),
    INVALID_FORM_STATUS_UPDATE("Bad request: Form status can't be updated"),
    INVALID_LOCATION_IS_TOO_FAR("Bad request: Location is too far from customer"),
    CUSTOMER_NOT_IN_CITY("Bad request: customer is not in the visit's city"),
    DATE_IN_PAST("Bad request: date must be in the present or future"),
    DATE_TOO_OLD("Bad request: date has already passed"),
    PASSWORD_REQUEST_STILL_PENDING("Bad request: your last request is still pending admin approval"),
    PASSWORD_REQUEST_TOO_EARLY("Bad request: your last request was in the last two weeks, please wait"),


    // CONFLICT EXCEPTIONS
    USER_ALREADY_EXISTS("Conflict: Username already exists"),
    CUSTOMER_ALREADY_ASSIGNED("Conflict: Customer is already assigned"),

    TEMPLATE_NOT_FOUND("Not found: Question template not found"),
    FORM_NOT_A_SURVEY("Not found: Visit Form not a survey");


    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }


}
