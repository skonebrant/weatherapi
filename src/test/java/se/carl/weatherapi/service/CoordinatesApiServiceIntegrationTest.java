package se.carl.weatherapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import se.carl.weatherapi.dto.CoordinatesResponse;
import se.carl.weatherapi.exception.RestClientCustomException;

@SpringBootTest
class CoordinatesApiServiceIntegrationTest {

	@MockitoBean
	private RestTemplate restTemplate;

	@Autowired
	private CoordinatesApiService geoCodeApiService;

	@Value("${geocode.api.url}")
	private String apiUrl;

	@Value("${geocode.api.key}")
	private String apiKey;

	@Test
	void testGetGeoCode_Success() {
		CoordinatesResponse[] mockResponse = {new CoordinatesResponse("Stockholm", "59.3293", "18.0686", 0.9)};
		var expectedUrl = apiUrl + "?q=Stockholm&api_key=" + apiKey;
		when(restTemplate.getForObject(expectedUrl, CoordinatesResponse[].class)).thenReturn(mockResponse);

		var response = geoCodeApiService.getGeoCode("Stockholm");

		assertEquals("Stockholm", response.cityName());
		assertEquals("59.3293", response.latitude());
		assertEquals("18.0686", response.longitude());
		assertEquals(0.9, response.importance());
	}

	@Test
	void testGetGeoCode_NoResponse() {
		var expectedUrl = apiUrl + "?q=UnknownCity&api_key=" + apiKey;
		when(restTemplate.getForObject(expectedUrl, CoordinatesResponse[].class)).thenReturn(null);

		var exception = assertThrows(RestClientCustomException.class, () -> {
			geoCodeApiService.getGeoCode("UnknownCity");
		});

		assertEquals("No response from Coordinates API for city: UnknownCity", exception.getMessage());
		assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatus());
	}

	@Test
	void testGetGeoCode_RestClientException() {
		var expectedUrl = apiUrl + "?q=Stockholm&api_key=" + apiKey;
		when(restTemplate.getForObject(expectedUrl, CoordinatesResponse[].class))
				.thenThrow(new RestClientException("API error"));

		var exception = assertThrows(RestClientCustomException.class, () -> {
			geoCodeApiService.getGeoCode("Stockholm");
		});

		assertEquals("Error fetching Coordinate data: API error", exception.getMessage());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getStatus());
	}
}
