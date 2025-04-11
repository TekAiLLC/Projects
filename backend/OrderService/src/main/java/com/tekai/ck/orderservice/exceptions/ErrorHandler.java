/*
 * Copyright (c) 2025 Tek-AI LLC
 * All rights reserved.
 *
 * Created on 11-Apr-2025.
 */

package com.tekai.ck.orderservice.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ErrorHandler
{
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(final ResourceNotFoundException ex,
            final WebRequest request)
    {
        final ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false), HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStatusArgument.class)
    public ResponseEntity<ErrorDetails> handleIllegalStatusArgument(final IllegalStatusArgument ex,
            final WebRequest request)
    {
        final ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false), HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(final Exception ex, final WebRequest request)
    {
        final ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Error details class
    public static class ErrorDetails
    {
        private final LocalDateTime timestamp;
        private final String message;
        private final String details;
        private final int status;

        public ErrorDetails(final LocalDateTime timestamp, final String message, final String details, final int status)
        {
            this.timestamp = timestamp;
            this.message = message;
            this.details = details;
            this.status = status;
        }

        // Getters
        public LocalDateTime getTimestamp()
        {
            return timestamp;
        }

        public String getMessage()
        {
            return message;
        }

        public String getDetails()
        {
            return details;
        }

        public int getStatus()
        {
            return status;
        }
    }
}
