package me.hjeong.mojji.infra.formatter;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.station.Station;
import me.hjeong.mojji.module.station.StationService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class StationFormatter implements Formatter<Station> {

    private final StationService stationService;

    @Override
    public Station parse(String s, Locale locale) throws ParseException {
        return stationService.getStationByString(s);
    }

    @Override
    public String print(Station station, Locale locale) {
        return station.toString();
    }
}
