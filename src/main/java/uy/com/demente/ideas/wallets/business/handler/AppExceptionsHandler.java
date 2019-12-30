package uy.com.demente.ideas.wallets.business.handler;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import uy.com.demente.ideas.wallets.business.exceptions.*;
import uy.com.demente.ideas.wallets.model.response.ErrorMessage;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
//extends ResponseEntityExceptionHandler --> ambiguous MethodArgumentNotValidException
public class AppExceptionsHandler  {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException
            (NotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(this.getErrorMessage(ex), new HttpHeaders(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException
            (BadRequestException ex, WebRequest request) {
        return new ResponseEntity<>(this.getErrorMessage(ex), new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {PaymentRequiredException.class})
    public ResponseEntity<Object> handlePaymentRequiredException
            (PaymentRequiredException ex, WebRequest request) {
        return new ResponseEntity<>(this.getErrorMessage(ex), new HttpHeaders(),
                HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<Object> handleUnauthorizedException
            (UnauthorizedException ex, WebRequest request) {
        return new ResponseEntity<>(this.getErrorMessage(ex), new HttpHeaders(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {InternalServerErrorException.class})
    public ResponseEntity<Object> handleException
            (InternalServerErrorException ex, WebRequest request) {
        return new ResponseEntity<>(this.getErrorMessage(ex), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param ex
     * @return
     */
    private ErrorMessage getErrorMessage(Exception ex) {
        String message = ex.getLocalizedMessage();
        if (message == null) {
            message = ex.toString();
        }
        return new ErrorMessage(message, LocalDateTime.now());
    }

    //////////////////////////////////////////////////////
    /////////////////////// @Valid ///////////////////////
    //////////////////////////////////////////////////////

    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions
            (MethodArgumentNotValidException ex) {

        String messageErrors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));

        return new ResponseEntity<>(new ErrorMessage(messageErrors, LocalDateTime.now()), new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }
}
