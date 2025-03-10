package se.carl.weatherapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder(toBuilder = true)
public record CoordinatesResponse(@JsonProperty("display_name") String cityName,
        @JsonProperty("lat") String latitude, @JsonProperty("lon") String longitude,
        double importance) {
}
