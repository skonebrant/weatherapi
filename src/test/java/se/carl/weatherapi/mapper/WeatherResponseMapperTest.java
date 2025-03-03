package se.carl.weatherapi.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import se.carl.weatherapi.dto.WeatherApiResponse;
import se.carl.weatherapi.dto.WeatherApiResponse.*;

class WeatherResponseMapperTest {

	private final WeatherResponseMapper weatherMapper =
			Mappers.getMapper(WeatherResponseMapper.class);

	@Test
	void testMapWeatherApiResponseToWeatherResponse() {
		var details = new WeatherApiResponse.Details(15.0, 5.0, 50.0, 1013.0, 80.0, 180.0, 0.0);
		var instant = new Instant(details);
		var next1Hour = new Next1Hour(new Summary("clearsky_day"), Map.of());
		var data = new Data(instant, null, next1Hour, null);
		var timeSeries = new TimeSeries("2025-03-03T12:00:00Z", data);
		var weatherApiResponse =
				new WeatherApiResponse(new WeatherApiResponse.Properties(List.of(timeSeries)));

		var weatherResponse =
				weatherMapper.mapWeatherApiResponseToWeatherResponse("Stockholm", weatherApiResponse);

		assertEquals("Stockholm", weatherResponse.cityName());
		assertEquals(1, weatherResponse.forecast().size());

		var forecast = weatherResponse.forecast().get(0);
		assertEquals("12:00 PM", forecast.time());
		assertEquals(15.0, forecast.temperature());
		assertEquals("Clear Sky", forecast.weather().description());
		assertEquals(0.0, forecast.precipitation().amount());
		assertEquals(false, forecast.precipitation().isRain());
		assertEquals(5.0, forecast.wind().speed());
		assertEquals("180 degrees (South)", forecast.wind().direction());
	}

	@Test
	void testTimeSeriesToForecast() {
		var details = new WeatherApiResponse.Details(15.0, 5.0, 50.0, 1013.0, 80.0, 180.0, 0.0);
		var instant = new Instant(details);
		var next1Hour = new Next1Hour(new Summary("clearsky_day"), Map.of());
		var data = new Data(instant, null, next1Hour, null);
		var timeSeries = new TimeSeries("2025-03-03T12:00:00Z", data);

		var forecast = weatherMapper.timeSeriesToForecast(timeSeries);

		assertEquals("12:00 PM", forecast.time());
		assertEquals(15.0, forecast.temperature());
		assertEquals("Clear Sky", forecast.weather().description());
		assertEquals(0.0, forecast.precipitation().amount());
		assertEquals(false, forecast.precipitation().isRain());
		assertEquals(5.0, forecast.wind().speed());
		assertEquals("180 degrees (South)", forecast.wind().direction());
	}
}
