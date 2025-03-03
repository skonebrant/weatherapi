package se.carl.weatherapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import se.carl.weatherapi.exception.RestClientCustomException;

@SpringBootTest
class WeatherApiServiceIntegrationTest {

	@MockitoBean
	private RestTemplate restTemplate;

	@Autowired
	private WeatherApiService weatherApiService;

	@Value("${yr.api.url}")
	private String apiUrl;

	@Value("${yr.api.user-agent}")
	private String userAgent;

	@Test
	void testGetWeatherData_Success() {
		String mockResponse = "{\"weather\":\"sunny\"}";
		var expectedUrl = apiUrl + "?lat=59.3293&lon=18.0686";
		when(restTemplate.getForObject(expectedUrl, String.class)).thenReturn(mockResponse);

		var response = weatherApiService.getWeatherData("59.3293", "18.0686");

		assertEquals(mockResponse, response);
	}

	@Test
	void testGetWeatherData_NoResponse() {
		var expectedUrl = apiUrl + "?lat=59.3293&lon=18.0686";
		when(restTemplate.getForObject(expectedUrl, String.class)).thenReturn(null);

		var exception = assertThrows(RestClientCustomException.class, () -> {
			weatherApiService.getWeatherData("59.3293", "18.0686");
		});

		assertEquals("Error fetching weather data: Weather data not found", exception.getMessage());
		assertEquals(500, exception.getStatus());
	}

	@Test
	void testGetWeatherData_RestClientException() {
		var expectedUrl = apiUrl + "?lat=59.3293&lon=18.0686";
		when(restTemplate.getForObject(expectedUrl, String.class))
				.thenThrow(new RestClientException("API error"));

		var exception = assertThrows(RestClientCustomException.class, () -> {
			weatherApiService.getWeatherData("59.3293", "18.0686");
		});

		assertEquals("Error fetching weather data: API error", exception.getMessage());
		assertEquals(500, exception.getStatus());
	}
}
