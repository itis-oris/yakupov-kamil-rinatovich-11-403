package com.arsaka.auth.controller.handler;

import com.arsaka.auth.exception.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiException> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, List<String>> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));

        return ResponseEntity
                .status(SC_BAD_REQUEST)
                .body(ApiException.ofValidation(
                        SC_BAD_REQUEST,
                        errors,
                        request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiException> handleNotReadable(HttpMessageNotReadableException exception, HttpServletRequest request) {

        if (exception.getCause() instanceof InvalidFormatException ife) {
            Object value = ife.getValue();

            return ResponseEntity.
                    status(SC_BAD_REQUEST)
                    .body(ApiException.of(
                            SC_BAD_REQUEST,
                            "Invalid value '%s'".formatted(value),
                            request.getRequestURI()
                    ));
        }

        return ResponseEntity.
                status(SC_BAD_REQUEST)
                .body(ApiException.of(
                        SC_BAD_REQUEST,
                        "Invalid request body",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiException> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {

        String field = exception.getName();
        Object value = exception.getValue();

        String requiredType = exception.getRequiredType() != null
                ? exception.getRequiredType().getSimpleName()
                : "unknown";

        String message = String.format(
                "Invalid value '%s' for field '%s'. Expected type: %s",
                value,
                field,
                requiredType
        );

        return ResponseEntity.badRequest().body(
                ApiException.of(
                        SC_BAD_REQUEST,
                        message,
                        request.getRequestURI()
                )
        );
    }

    @ExceptionHandler({
            InvalidSessionException.class,
            BadCredentialsException.class,
            JwtValidException.class,
            MissingRequestCookieException.class
    })
    public final ResponseEntity<ApiException> handleAuthException(Exception exception, HttpServletRequest request) {
        return ResponseEntity
                .status(SC_UNAUTHORIZED)
                .body(ApiException.of(
                                SC_UNAUTHORIZED,
                                exception.getMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler({EmailAlreadyExistsException.class, UsernameAlreadyExistsException.class})
    public final ResponseEntity<ApiException> handleEmailException(Exception exception, HttpServletRequest request) {
        return ResponseEntity
                .status(SC_CONFLICT)
                .body(ApiException.of(
                                SC_CONFLICT,
                                exception.getMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(AccountNotActiveException.class)
    public final ResponseEntity<ApiException> handleAccountNotActiveException(HttpServletRequest request) {
        return ResponseEntity
                .status(SC_FORBIDDEN)
                .body(ApiException.of(
                                SC_FORBIDDEN,
                                "Account not active",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public final ResponseEntity<ApiException> handleTooManyRequestsException(HttpServletRequest request) {
        return ResponseEntity
                .status(429)
                .body(ApiException.of(
                                429,
                                "Too many requests, please try again",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler({
            AccountNotFoundException.class,
            NoSuchDefaultAccountRoleException.class
    })
    public final ResponseEntity<ApiException> handleNoSuchDefaultRoleException(Exception exception, HttpServletRequest request) {
        return ResponseEntity
                .status(SC_NOT_FOUND)
                .body(ApiException.of(
                                SC_NOT_FOUND,
                                exception.getMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiException> handleAllExceptions(Exception exception, HttpServletRequest request) {
        log.error("Unhandled server exception | path={} | exception={}",
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                exception);

        return ResponseEntity
                .status(SC_INTERNAL_SERVER_ERROR)
                .body(ApiException.of(
                                SC_INTERNAL_SERVER_ERROR,
                                "error, please try again",
                                request.getRequestURI()
                        )
                );
    }
}
