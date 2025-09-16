package co.com.pragma.consumer.auth.exception;

public class InvalidTokenUnauthorizedException extends UnauthorizedException {

    public InvalidTokenUnauthorizedException(String message) {
        super(message);
    }
}
