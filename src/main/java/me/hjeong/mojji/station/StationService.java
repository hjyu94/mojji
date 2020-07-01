package me.hjeong.mojji.station;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.domain.Station;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StationService {

    private final StationRepository stationRepository;
    private final ResourceLoader resourceLoader;

    @PostConstruct
    public void initZoneData() throws IOException {
        if (stationRepository.count() == 0) {
            try (InputStream resource = resourceLoader.getResource("classpath:station.csv").getInputStream()) {
                List<String> strings = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8))
                        .lines().collect(Collectors.toList());

                List<Station> zoneList =
                        strings.stream()
                                .map(line -> {
                                    String[] split = line.split(",");
                                    return Station.builder().region(split[0]).line(split[1]).name(split[2]).build();
                                })
                                .collect(Collectors.toList());
                stationRepository.saveAll(zoneList);
            }
        }
    }

    public Station getStationByString(String stationStr) {
        String pattern = "[()-]";
        String[] split = stationStr.split(pattern);
        Optional<Station> optStation = stationRepository.findByRegionAndLineAndName(split[1], split[2], split[0]);
        return optStation.orElse(null);
    }
}
