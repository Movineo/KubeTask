package com.movine.taskmanagementsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus; /**
 * Custom exception class for handling internal server errors.
 * This exception is thrown when an unexpected condition was encountered.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {

    /**
     * Constructs a new InternalServerErrorException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public InternalServerErrorException(String message) {
        super(message);
    }

    /**
     * Constructs a new InternalServerErrorException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause the cause (which is saved for later retrieval by the getCause() method).
     *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
