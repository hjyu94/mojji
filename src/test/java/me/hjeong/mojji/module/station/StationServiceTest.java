package me.hjeong.mojji.module.station;

import me.hjeong.mojji.infra.config.DataJpaConfig;
import me.hjeong.mojji.infra.config.ServiceConfig;
import me.hjeong.mojji.module.account.Account;
import me.hjeong.mojji.infra.factory.AccountFactory;
import me.hjeong.mojji.infra.factory.StationFactory;
import me.hjeong.mojji.module.station.Station;
import me.hjeong.mojji.module.station.StationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({DataJpaConfig.class, ServiceConfig.class})
class StationServiceTest {

    @Autowired AccountFactory accountFactory;
    @Autowired StationFactory stationFactory;
    @Autowired
    StationService stationService;

    private static Stream<Arguments> stringArrayProvider() {
        return Stream.of(
                Arguments.of(List.of()),
                Arguments.of(List.of("1(지역-호선)", "2(지역-호선)")),
                Arguments.of(List.of("1역(지역-호선)", "2역(지역-호선)"))
        );
    }

    @ParameterizedTest
    @MethodSource("stringArrayProvider")
    @DisplayName("프로필에 동네 표시하기")
    void parseFirstAndSecondInt(List<String> stations) {
        for(String stationStr : stations) {
            stationFactory.createStation(stationStr);
        }

        Account account = accountFactory.getOne();
        for(String stationStr : stations) {
            Station station = stationService.getStationByString(stationStr);
            account.getStations().add(station);
        }

        String stationsString = account.getStationsString();
        if(stations.isEmpty()) {
            assertEquals("", stationsString);
        } else {
            assertEquals("1역, 2역", stationsString);
        }
    }

}