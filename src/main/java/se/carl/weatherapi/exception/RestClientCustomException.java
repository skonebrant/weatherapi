package se.carl.weatherapi.exception;

public class RestClientCustomException extends RuntimeException {

	private final int status;

	public RestClientCustomException(String message, int status) {
		super(message);
		this.status = status;
	}

	public RestClientCustomException(String message, int status, Throwable cause) {
		super(message, cause);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
