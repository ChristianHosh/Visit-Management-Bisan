package com.example.vm.controller.error;

import com.example.vm.controller.error.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({
            PasswordDoesntMatchException.class,
            NoContactTypeException.class,
            InvalidStatusUpdateException.class,
            LocationTooFarException.class,
            EntityNotEnabled.class,
            InvalidPasswordResetException.class
    })
    public ResponseEntity<ApiError> handleBadRequestExceptions(Exception exception){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler({
            UserAlreadyExistsException.class,
            CustomerAlreadyAssignedException.class
    })
    public ResponseEntity<ApiError> handleConflictExceptions(Exception exception){
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler({
            LocationNotFoundException.class,
            EntityNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFoundExceptions(Exception exception){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException exception){
        String message = parseErrorMessage(exception.getMostSpecificCause().getMessage());
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, message);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> handleOthers(Exception exception) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    private static String parseErrorMessage(String errorMessage) {
        int index = errorMessage.indexOf("for key");
        return index != -1 ? errorMessage.substring(0, index).trim() : errorMessage;

    }

}


