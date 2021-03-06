package edu.pw.shoppingm8.receipt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MultipartFileException extends RuntimeException {
    public MultipartFileException() {
        super("Invalid file");
    }
}
