package se.carl.weatherapi.service;

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
import se.carl.weatherapi.mapper.WeatherMapper;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final CoordinatesDataService geoCodeService;
    private final WeatherDataService weatherDataService;
    private final WeatherMapper weatherMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherResponse getWeather(String location) {
        var geoCode = geoCodeService.getGeoCode(location);

        var yrApiResponse =
                weatherDataService.getWeatherData(geoCode.latitude(), geoCode.longitude());

        return weatherMapper.mapWeatherApiResponseToWeatherResponse(location,
                parseApiResponse(yrApiResponse));
    }

    private WeatherApiResponse parseApiResponse(String apiResponse) {
        try {
            return objectMapper.readValue(apiResponse, WeatherApiResponse.class);
        } catch (JsonMappingException e) {
            throw new JsonProcessCustomException(
                    "Error mapping JSON to WeatherApiResponse: " + e.getMessage(), 400, e);
        } catch (JsonProcessingException e) {
            throw new JsonProcessCustomException("Error processing JSON: " + e.getMessage(), 400,
                    e);
        } catch (Exception e) {
            throw new RestClientCustomException("Error fetching weather data: " + e.getMessage(),
                    500, e);
        }
    }
}
