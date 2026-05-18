package com.arsaka.exception.handler;

import com.arsaka.auth.exception.ApiException;
import com.arsaka.auth.exception.AuthServiceException;
import com.arsaka.auth.exception.JwtValidException;
import com.arsaka.exception.*;
import com.arsaka.flightsearch.exception.CursorDecodeException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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
    public ResponseEntity<ApiException> handleBodyValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiException> handleParamValidation(ConstraintViolationException exception, HttpServletRequest request) {
        Map<String, List<String>> errors = exception.getConstraintViolations()
                .stream()
                .collect(Collectors.groupingBy(
                        v -> v.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
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

        return ResponseEntity.
                status(SC_BAD_REQUEST)
                .body(
                ApiException.of(
                        SC_BAD_REQUEST,
                        message,
                        request.getRequestURI()
                )
        );
    }

    @ExceptionHandler(MissingRequestCookieException.class)
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

    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<ApiException> handleAuthServiceException(AuthServiceException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ex.getApiException());
    }


    @ExceptionHandler(CursorDecodeException.class)
    public final ResponseEntity<ApiException> handleCursorException(HttpServletRequest request) {
        return ResponseEntity
                .status(SC_BAD_REQUEST)
                .body(ApiException.of(
                                SC_BAD_REQUEST,
                                "Cursor is not valid",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ApiException> handleBadRequestException(BadRequestException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(SC_BAD_REQUEST)
                .body(ApiException.of(
                                SC_BAD_REQUEST,
                                exception.getMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ApiException> handleNotFoundException(NotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(SC_NOT_FOUND)
                .body(ApiException.of(
                                SC_NOT_FOUND,
                                exception.getMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(ConflictException.class)
    public final ResponseEntity<ApiException> handleConflictException(ConflictException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(SC_CONFLICT)
                .body(ApiException.of(
                                SC_CONFLICT,
                                exception.getMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ApiException> handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(SC_FORBIDDEN)
                .body(ApiException.of(
                                SC_FORBIDDEN,
                                exception.getMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public final ResponseEntity<ApiException> handleServiceUnavailableException(ServiceUnavailableException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(SC_SERVICE_UNAVAILABLE)
                .body(ApiException.of(
                                SC_SERVICE_UNAVAILABLE,
                                exception.getMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(ServiceException.class)
    public final ResponseEntity<ApiException> handleServiceException( HttpServletRequest request) {
        return ResponseEntity
                .status(SC_SERVICE_UNAVAILABLE)
                .body(ApiException.of(
                                SC_SERVICE_UNAVAILABLE,
                                "Service cannot process request, please try again later",
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
