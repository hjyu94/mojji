package me.hjeong.mojji.infra.factory;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.station.Station;
import me.hjeong.mojji.module.station.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StationFactory {

    @Autowired
    StationRepository stationRepository;

    public Station createStation(String strStation) {
        String pattern = "[()-]";
        String[] split = strStation.split(pattern);
        return createStation(split[1], split[2], split[0]);
    }

    public Station createStation(String region, String line, String name) {
        Station station = Station.builder()
                .region(region).line(line).name(name)
                .build();
        return stationRepository.save(station);
    }

}