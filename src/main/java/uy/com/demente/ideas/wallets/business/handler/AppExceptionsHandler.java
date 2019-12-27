package uy.com.demente.ideas.wallets.business.handler;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uy.com.demente.ideas.wallets.business.exceptions.InternalServerErrorException;
import uy.com.demente.ideas.wallets.business.exceptions.NotFoundException;
import uy.com.demente.ideas.wallets.business.exceptions.UserNotFoundException;
import uy.com.demente.ideas.wallets.model.response.ErrorMessage;

import java.time.LocalDateTime;

@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException
            (NotFoundException ex, WebRequest request) {

        String message = ex.getLocalizedMessage();
        if(message == null) {
            ex.toString();
        }

        ErrorMessage errorMessage = new ErrorMessage(message, LocalDateTime.now());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InternalServerErrorException.class})
    public ResponseEntity<Object> handleException
            (InternalServerErrorException ex, WebRequest request) {

        String message = ex.getLocalizedMessage();
        if(message == null) {
            message = "Internal Server Error";
        }

        ErrorMessage errorMessage = new ErrorMessage(message, LocalDateTime.now());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
