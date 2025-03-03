package se.carl.weatherapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import se.carl.weatherapi.dto.CoordinatesResponse;
import se.carl.weatherapi.exception.RestClientCustomException;
import se.carl.weatherapi.interfaces.CoordinatesDataService;

@Service
@RequiredArgsConstructor
public class CoordinatesApiService implements CoordinatesDataService {

    @Value("${geocode.api.key}")
    private String apiKey;

    @Value("${geocode.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Override
    public CoordinatesResponse getGeoCode(String cityName) {
        var url = UriComponentsBuilder.fromUriString(apiUrl).queryParam("q", cityName)
                .queryParam("api_key", apiKey).toUriString();

        try {
            var responses = restTemplate.getForObject(url, CoordinatesResponse[].class);

            if (responses == null || responses.length == 0) {
                throw new RestClientCustomException(
                        "No response from Coordinates API for city: " + cityName,
                        HttpStatus.NOT_FOUND.value());
            }

            var response = responses[0];

            return new CoordinatesResponse(response.cityName(), response.latitude(),
                    response.longitude(), response.importance());

        } catch (RestClientException e) {
            throw new RestClientCustomException("Error fetching Coordinate data: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
