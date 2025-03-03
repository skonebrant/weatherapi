package se.carl.weatherapi.mapper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import se.carl.weatherapi.dto.WeatherApiResponse;
import se.carl.weatherapi.dto.WeatherResponse;
import se.carl.weatherapi.dto.WeatherApiResponse.TimeSeries;
import se.carl.weatherapi.dto.WeatherResponse.Forecast;
import se.carl.weatherapi.dto.WeatherResponse.Forecast.Precipitation;
import se.carl.weatherapi.dto.WeatherResponse.Forecast.Weather;
import se.carl.weatherapi.dto.WeatherResponse.Forecast.Wind;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    WeatherMapper INSTANCE = Mappers.getMapper(WeatherMapper.class);

    @Mapping(source = "weatherApiResponse.properties.timeseries", target = "forecast")
    WeatherResponse mapWeatherApiResponseToWeatherResponse(String cityName,
            WeatherApiResponse weatherApiResponse);

    default Forecast timeSeriesToForecast(TimeSeries timeSeries) {
        return new Forecast(formatTime(timeSeries.time()), extractTemperature(timeSeries),
                new Weather(getWeatherDescription(extractWeatherDescription(timeSeries))),
                new Precipitation(extractPrecipitationAmount(timeSeries),
                        mapPrecipitationToIsRain(extractPrecipitationAmount(timeSeries))),
                new Wind(extractWindSpeed(timeSeries),
                        String.valueOf(extractWindDirectionWithText(timeSeries))));
    }

    private double extractTemperature(TimeSeries timeSeries) {
        return timeSeries.data().instant().details().air_temperature();
    }

    private double extractWindSpeed(TimeSeries timeSeries) {
        return timeSeries.data().instant().details().wind_speed();
    }

    private double extractPrecipitationAmount(TimeSeries timeSeries) {
        return Optional.ofNullable(timeSeries.data().instant().details().precipitation_amount())
                .orElse(0.0);
    }

    private String extractWeatherDescription(TimeSeries timeSeries) {
        return Optional.ofNullable(timeSeries.data().next_1_hours())
                .map(next -> next.summary().symbol_code()).orElse("Unknown Weather");
    }

    private boolean mapPrecipitationToIsRain(Double precipitationAmount) {
        return precipitationAmount > 0;
    }

    private String getWeatherDescription(String symbolCode) {
        return switch (symbolCode) {
            case "clearsky_day" -> "Clear Sky";
            case "partlycloudy_night" -> "Partly Cloudy Night";
            case "partlycloudy_day" -> "Partly Cloudy Day";
            case "cloudy" -> "Cloudy";
            case "fair_day" -> "Fair Day";
            case "fair_night" -> "Fair Night";
            default -> "Unknown Weather";
        };
    }

    private String extractWindDirectionWithText(TimeSeries timeSeries) {

        var windDirection = timeSeries.data().instant().details().wind_from_direction();

        return String.format("%.0f degrees (%s)", windDirection,
                switch ((int) ((windDirection + 22.5) / 45) % 8) {
                    case 0 -> "North";
                    case 1 -> "North-East";
                    case 2 -> "East";
                    case 3 -> "South-East";
                    case 4 -> "South";
                    case 5 -> "South-West";
                    case 6 -> "West";
                    case 7 -> "North-West";
                    default -> "Unknown";
                });
    }

    private String formatTime(String isoTime) {
        var dateTime = ZonedDateTime.parse(isoTime);
        var formatter = DateTimeFormatter.ofPattern("h:mm a");
        return dateTime.format(formatter);
    }
}
