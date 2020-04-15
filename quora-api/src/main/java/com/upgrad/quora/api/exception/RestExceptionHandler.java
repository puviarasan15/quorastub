package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    /*
     * Exception class = AuthenticationFailedException
     * Return Type = ResponseEntity<ErrorResponse> and the corresponding message along with HTTP status
     * Parameters = the webrequest and then AuthenticationFailedException
     * Description = This method will accept the parameters and create the AuthenticationFailedException and then return the ErrorResponse along with its parameters
     *  */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED
        );
    }

    /*
     * Exception class = AnswerNotFoundException
     * Return Type = ResponseEntity<ErrorResponse> and the corresponding message along with HTTP status
     * Parameters = the webrequest and then AnswerNotFoundException
     * Description = This method will accept the parameters and create the AnswerNotFoundException and then return the ErrorResponse along with its parameters
     *  */
    @ExceptionHandler(AnswerNotFoundException.class)
    public ResponseEntity<ErrorResponse> answerNotFoundException(AnswerNotFoundException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND
        );
    }

    /*
     * Exception class = InvalidQuestionException
     * Return Type = ResponseEntity<ErrorResponse> and the corresponding message along with HTTP status
     * Parameters = the webrequest and then InvalidQuestionException
     * Description = This method will accept the parameters and create the InvalidQuestionException and then return the ErrorResponse along with its parameters
     *  */
    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ErrorResponse> invalidQuestionException(InvalidQuestionException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND
        );
    }


    /*
     * Exception class = SignOutRestrictedException
     * Return Type = ResponseEntity<ErrorResponse> and the corresponding message along with HTTP status
     * Parameters = the webrequest and then SignOutRestrictedException
     * Description = This method will accept the parameters and create the SignOutRestrictedException and then return the ErrorResponse along with its parameters
     *  */
    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signOutRestrictedException(SignOutRestrictedException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED
        );
    }


    /*
     * Exception class = SignUpRestrictedException
     * Return Type = ResponseEntity<ErrorResponse> and the corresponding message along with HTTP status
     * Parameters = the webrequest and then SignUpRestrictedException
     * Description = This method will accept the parameters and create the SignUpRestrictedException and then return the ErrorResponse along with its parameters
     *  */
    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.CONFLICT
        );
    }

    /*
     * Exception class = AuthorizationFailedException
     * Return Type = ResponseEntity<ErrorResponse> and the corresponding message along with HTTP status
     * Parameters = the webrequest and then AuthorizationFailedException
     * Description = This method will accept the parameters and create the AuthorizationFailedException and then return the ErrorResponse along with its parameters
     *  */
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.FORBIDDEN);
    }

    /*
     * Exception class = UserNotFoundException
     * Return Type = ResponseEntity<ErrorResponse> and the corresponding message along with HTTP status
     * Parameters = the webrequest and then UserNotFoundException
     * Description = This method will accept the parameters and create the UserNotFoundException and then return the ErrorResponse along with its parameters
     *  */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

}