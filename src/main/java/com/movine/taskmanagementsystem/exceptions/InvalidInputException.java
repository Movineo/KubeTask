package com.movine.taskmanagementsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus; /**
 * Custom exception class for handling invalid input parameters.
 * This exception is thrown when the input parameters do not meet the expected criteria.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {

    /**
     * Constructs a new InvalidInputException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public InvalidInputException(String message) {
        super(message);
    }
}
