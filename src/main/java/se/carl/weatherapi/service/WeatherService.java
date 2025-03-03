package se.carl.weatherapi.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import se.carl.weatherapi.dto.WeatherApiResponse;
import se.carl.weatherapi.dto.WeatherResponse;
import se.carl.weatherapi.exception.JsonProcessCustomException;
import se.carl.weatherapi.exception.RestClientCustomException;
import se.carl.weatherapi.interfaces.CoordinatesDataService;
import se.carl.weatherapi.interfaces.WeatherDataService;
import se.carl.weatherapi.mapper.WeatherResponseMapper;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final CoordinatesDataService coordinatesDataService;
    private final WeatherDataService weatherDataService;
    private final WeatherResponseMapper weatherMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherResponse getWeather(String location) {
        var coordinates = coordinatesDataService.getCoordinatesFromApiResponse(location);

        var weatherApiResponse =
                weatherDataService.getWeatherDataFromApi(coordinates.latitude(), coordinates.longitude());

        return weatherMapper.mapWeatherApiResponseToWeatherResponse(location,
                parseApiResponse(weatherApiResponse));
    }

    private WeatherApiResponse parseApiResponse(String apiResponse) {
        try {
            return objectMapper.readValue(apiResponse, WeatherApiResponse.class);
        } catch (JsonMappingException e) {
            throw new JsonProcessCustomException(
                    "Error mapping JSON to WeatherApiResponse: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST.value(), e);
        } catch (JsonProcessingException e) {
            throw new JsonProcessCustomException("Error processing JSON: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST.value(), e);
        } catch (Exception e) {
            throw new RestClientCustomException("Error fetching weather data: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
