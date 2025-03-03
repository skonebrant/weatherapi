package se.carl.weatherapi.interfaces;

public interface WeatherDataService {
    String getWeatherDataFromApi(String latitude, String longitude);
}
