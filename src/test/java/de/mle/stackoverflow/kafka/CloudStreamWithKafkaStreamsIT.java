package de.mle.stackoverflow.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class CloudStreamWithKafkaStreamsIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Test
    @Disabled
    void process() {
        // given
        initTestQueueReceiverForTopic("counts");

        // when
        sendMessage("Wort Buch Wort", "first-words", "words");

        // then
        Awaitility.await().untilAsserted(() ->
                assertThat(records.stream()
                        .map(ConsumerRecord::value)
                        .filter(Objects::nonNull)
                        .map(this::parseValue))
                        .contains(new WordCount("Buch", 1l), new WordCount("Wort", 2l)));
    }

    @SneakyThrows
    private WordCount parseValue(String v) {
        return new ObjectMapper().readValue(v, WordCount.class);
    }
}
