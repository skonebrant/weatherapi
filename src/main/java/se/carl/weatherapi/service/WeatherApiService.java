package se.carl.weatherapi.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import se.carl.weatherapi.exception.RestClientCustomException;
import se.carl.weatherapi.interfaces.WeatherDataService;

@Service
@RequiredArgsConstructor
public class WeatherApiService implements WeatherDataService {

    private final RestTemplate restTemplate;

    @Value("${yr.api.url}")
    private String apiUrl;

    @Value("${yr.api.user-agent}")
    private String userAgent;

    @Override
    public String getWeatherData(String latitude, String longitude) {
        var urlWithParams = apiUrl + "?lat=" + latitude + "&lon=" + longitude;

        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("User-Agent", userAgent);
            return execution.execute(request, body);
        });

        try {
            String response = restTemplate.getForObject(urlWithParams, String.class);
            return Optional.ofNullable(response)
                    .orElseThrow(() -> new RestClientCustomException("Weather data not found",
                            HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            throw new RestClientCustomException("Error fetching weather data: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
