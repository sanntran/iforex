package net.ionoff.forex.ea.controller;

import lombok.extern.slf4j.Slf4j;
import net.ionoff.forex.ea.exception.MethodNotSupportedException;
import net.ionoff.forex.ea.exception.RecordNotFoundException;
import net.ionoff.forex.ea.exception.SymbolNotSupportedException;
import net.ionoff.forex.ea.model.ResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ResponseDto> handleRecordNotFoundException(RecordNotFoundException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.ok(ResponseDto.builder().code(HttpStatus.NOT_FOUND.value()).build());
    }

    @ExceptionHandler(SymbolNotSupportedException.class)
    public ResponseEntity<ResponseDto> handleSymbolNotSupportedException(SymbolNotSupportedException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.ok(ResponseDto.builder().code(HttpStatus.BAD_REQUEST.value()).build());
    }

    @ExceptionHandler(MethodNotSupportedException.class)
    public ResponseEntity<ResponseDto> handleMethodNotSupportedException(MethodNotSupportedException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.ok(ResponseDto.builder().code(HttpStatus.METHOD_NOT_ALLOWED.value()).build());
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
                                                          HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            log.error(ex.getMessage(), ex);
        } else {
            log.error(ex.getMessage());
        }
        return ResponseEntity.ok(ResponseDto.builder().code(status.value()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.ok(ResponseDto.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
    }
}
