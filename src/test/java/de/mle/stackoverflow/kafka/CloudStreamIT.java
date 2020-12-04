package de.mle.stackoverflow.kafka;

import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class CloudStreamIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Test
    void transformInts() {
        // given
        initTestQueueReceiverForTopic("second");

        // when the business code does its work

        // then
        Awaitility.await().untilAsserted(() ->
                assertThat(records.stream()
                        .map(ConsumerRecord::value)
                        .filter(Objects::nonNull))
                        .hasSizeGreaterThan(5));
    }
}
