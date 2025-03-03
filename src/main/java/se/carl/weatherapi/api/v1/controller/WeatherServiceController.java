package se.carl.weatherapi.api.v1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import se.carl.weatherapi.dto.WeatherResponse;
import se.carl.weatherapi.service.WeatherService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/weather")
public class WeatherServiceController {

    private final WeatherService weatherService;

    @GetMapping
    public WeatherResponse getWeatherByCityName(@RequestParam String cityName) {
        return weatherService.getWeather(cityName);
    }
}
