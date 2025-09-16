package co.com.pragma.usecase.metric.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message){
        super(message);
    }
}
