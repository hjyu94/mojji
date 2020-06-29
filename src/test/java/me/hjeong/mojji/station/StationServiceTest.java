package me.hjeong.mojji.station;

import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

@SpringBootTest
class StationServiceTest {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    StationService stationService;

    static List<Set<String>> stringArrayProvider() {
        return List.of(
                Set.of(new String[]{}),
                Set.of(new String[]{"종합운동장(수도권-09호선)", "서울역(수도권-경의선)"})
        );
    }

    @ParameterizedTest
    @MethodSource("stringArrayProvider")
    @DisplayName("프로필에 동네 표시하기")
    void parseFirstAndSecondInt(Set<String> stations) {
        Account account = new Account();
        for(String stationStr : stations) {
            Station station = stationService.getStationByString(stationStr);
            account.getStations().add(station);
        }
        System.out.println(account.getStationsString());
    }

}