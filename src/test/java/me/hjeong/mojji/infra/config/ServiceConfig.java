package me.hjeong.mojji.infra.config;

import me.hjeong.mojji.module.station.StationRepository;
import me.hjeong.mojji.module.station.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

@TestConfiguration
public class ServiceConfig {
    @Autowired StationRepository stationRepository;
    @Autowired ResourceLoader resourceLoader;
    @Autowired Environment environment;

    @Bean
    StationService stationService() {
        return new StationService(stationRepository, resourceLoader, environment);
    }
}
