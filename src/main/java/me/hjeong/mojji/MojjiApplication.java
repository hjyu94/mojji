package me.hjeong.mojji;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication
public class MojjiApplication {

    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        log.info("현재시각: {}", new Date());
    }

    public static void main(String[] args) {
        SpringApplication.run(MojjiApplication.class, args);
    }

}
