package de.mle.stackoverflow.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

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
                        .filter(Objects::nonNull)
                        .map(this::parseValue))
                        .contains(new SquareNumber(1), new SquareNumber(4), new SquareNumber(9)));
    }

    @SneakyThrows
    private SquareNumber parseValue(String v) {
        return new ObjectMapper().readValue(v, SquareNumber.class);
    }
}
