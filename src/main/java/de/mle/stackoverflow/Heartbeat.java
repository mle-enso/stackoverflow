package de.mle.stackoverflow;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Heartbeat {

    @Scheduled(fixedRate = 1_000)
    public void logRegularHeartbeat() {
        System.out.println("pulse");
    }
}