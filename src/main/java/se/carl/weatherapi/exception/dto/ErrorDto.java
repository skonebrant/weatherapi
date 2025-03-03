package se.carl.weatherapi.exception.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDto {
	private String message;
	private int status;
	private LocalDateTime timestamp;

	public ErrorDto(String message, int status) {
		this.message = message;
		this.status = status;
		this.timestamp = LocalDateTime.now();
	}
}
