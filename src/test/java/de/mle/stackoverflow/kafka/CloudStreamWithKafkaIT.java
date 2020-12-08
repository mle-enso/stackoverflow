package de.mle.stackoverflow.kafka;

import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class CloudStreamWithKafkaIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Test
    void producerProcessorConsumer() {
        // given
        initTestQueueReceiverForTopic("second");

        // when
        sendMessage(1, "number1", "first");
        sendMessage(2, "number2", "first");
        sendMessage(3, "number3", "first");

        // then
        Awaitility.await().untilAsserted(() ->
                assertThat(records.stream()
                        .map(ConsumerRecord::value)
                        .filter(Objects::nonNull))
                        .contains("1", "4", "9"));
    }
}
