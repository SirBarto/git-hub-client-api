package pl.bgawron.githubapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.bgawron.githubapi.exception.status.BadRequestException;
import pl.bgawron.githubapi.exception.status.NotFoundException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handlerApiBadRequestException(
            BadRequestException e)
    {
        //1. create payload containing exception details
        HttpStatus badRequest =  HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        //2. return response entity
        return new ResponseEntity<>(apiException,badRequest);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handlerApiNotFoundException(
            NotFoundException e)
    {
        //1. create payload containing exception details
        HttpStatus notFound =  HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        //2. return response entity
        return new ResponseEntity<>(apiException,notFound);
    }

}
