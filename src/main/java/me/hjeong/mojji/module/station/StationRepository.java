package me.hjeong.mojji.module.station;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface StationRepository extends JpaRepository<Station, Long> {
    List<Station> findAllByRegion(String region); // 지역
    List<Station> findAllByRegionAndLine(String region, String line); // 지역, 호선
    Optional<Station> findByRegionAndLineAndName(String region, String line, String name); // 지역, 호선, 역 이름
}
