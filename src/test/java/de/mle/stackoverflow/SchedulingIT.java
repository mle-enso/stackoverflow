package de.mle.stackoverflow;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchedulingIT {
    @SpyBean
    private Heartbeat heartbeat;

    @Test
    public void getScheduledHeartbeatAtLeast3TimesAlthoughAlternateMockMakerIsActive() {
        // given
        HeartBeatFinal heartBeatFinal = mock(HeartBeatFinal.class);
        when(heartBeatFinal.toString()).thenReturn("abc");

        // when
        // @Scheduled does its job

        // then
        Awaitility.await().atMost(Duration.of(10, SECONDS))
                .untilAsserted(() -> verify(heartbeat, atLeast(3)).logRegularHeartbeat());
    }
}