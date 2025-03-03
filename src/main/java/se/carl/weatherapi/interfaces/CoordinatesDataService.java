package se.carl.weatherapi.interfaces;

import se.carl.weatherapi.dto.CoordinatesResponse;

public interface CoordinatesDataService {
    CoordinatesResponse getCoordinatesFromApiResponse(String cityName);
}
