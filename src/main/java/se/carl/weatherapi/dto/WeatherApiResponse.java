package se.carl.weatherapi.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public record WeatherApiResponse(Properties properties) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Properties(List<TimeSeries> timeseries) {
        }

        public record TimeSeries(String time, Data data) {
        }

        public record Data(Instant instant, Next12Hours next_12_hours, Next1Hour next_1_hours,
                        Next6Hours next_6_hours) {
        }

        public record Instant(Details details) {
        }

        public record Details(Double air_temperature, Double wind_speed, Double cloud_area_fraction,
                        Double air_pressure_at_sea_level, Double relative_humidity,
                        Double wind_from_direction, Double precipitation_amount) {
        }

        public record Next12Hours(Summary summary, Map<String, Object> details) {
        }

        public record Next1Hour(Summary summary, Map<String, Object> details) {
        }

        public record Next6Hours(Summary summary, Map<String, Object> details) {
        }

        public record Summary(String symbol_code) {
        }
}
