package uz.billsplitter.demo.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApplicationException extends RuntimeException {
    private int code;
    private int status;
    private String errorType;
    private String message;

    public ApplicationException(int code , int status , String errorType , String message) {
        this.code = code;
        this.status = status;
        this.errorType = errorType;
        this.message = message;
    }
}
