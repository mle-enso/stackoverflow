package de.mle.stackoverflow.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CloudStreamWithKafkaStreamsIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Test
    @SneakyThrows
    void process() {
        // given
        initTestQueueReceiverForTopic("counts");

        // when
        sendMessage("Wort Buch Wort", null, "words");
        sendMessage("Auto Auto Wort", null, "words");
        Thread.sleep(6_000);
        sendMessage("Auto Affe", null, "words");

        // then
        Awaitility.await().untilAsserted(() ->
                assertThat(records.stream()
                        .map(ConsumerRecord::value)
                        .filter(Objects::nonNull)
                        .map(this::parseValue)
                        .collect(Collectors.toList())
                )
                        .containsExactlyInAnyOrder(
                                // from first time window
                                new WordCount("", 2l),
                                new WordCount("buch", 1l),
                                new WordCount("auto", 2l),
                                new WordCount("wort", 3l),
                                // from second time window
                                new WordCount("", 1l),
                                new WordCount("auto", 1l),
                                new WordCount("affe", 1l)
                                ));
    }

    @SneakyThrows
    private WordCount parseValue(String v) {
        return new ObjectMapper().readValue(v, WordCount.class);
    }
}
