package de.mle.stackoverflow.kafka;

import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class CloudStreamIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Test
    void producerProcessorConsumer() {
        // given
        initTestQueueReceiverForTopic("second");

        // when the business code does its work

        // then
        Awaitility.await().atMost(Duration.ofSeconds(30)).untilAsserted(() ->
                assertThat(records.stream()
                        .map(ConsumerRecord::value)
                        .filter(Objects::nonNull))
                        .hasSizeGreaterThan(5));
    }
}
