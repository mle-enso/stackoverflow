package de.mle.stackoverflow.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.util.List;
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
        sendMessage(new Words(List.of("Wort", "Buch", "Wort")), null, "words");
        sendMessage(new Words(List.of("Auto", "Auto", "Wort")), null, "words");
        Thread.sleep(6_000);
        sendMessage(new Words(List.of("Auto", "Affe")), null, "words");
       // sendMessage("peng", null, "words");

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
                                new WordCount("Buch", 1l),
                                new WordCount("Auto", 2l),
                                new WordCount("Wort", 3l),
                                // from second time window
                                new WordCount("Auto", 1l),
                                new WordCount("Affe", 1l)
                                ));
    }

    @SneakyThrows
    private WordCount parseValue(String v) {
        return new ObjectMapper().readValue(v, WordCount.class);
    }
}
