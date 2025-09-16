package co.com.pragma.consumer.auth.exception;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message){
        super(message);
    }
}
