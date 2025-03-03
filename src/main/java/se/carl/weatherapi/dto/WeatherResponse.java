package se.carl.weatherapi.dto;

import java.util.List;

public record WeatherResponse(String cityName, List<Forecast> forecast) {

        public record Forecast(String time, double temperature, Weather weather,
                        Precipitation precipitation, Wind wind) {

                public record Weather(String description) {
                }

                public record Precipitation(double amount, boolean isRain) {
                }

                public record Wind(double speed, String direction) {
                }
        }
}
