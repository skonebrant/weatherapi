package se.carl.weatherapi.exception;

public class JsonProcessCustomException extends RuntimeException {

    private final int statusCode;

    public JsonProcessCustomException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
