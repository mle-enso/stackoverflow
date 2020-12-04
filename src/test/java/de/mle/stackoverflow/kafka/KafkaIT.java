package de.mle.stackoverflow.kafka;

import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class KafkaIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Test
    void concatLists() {
        // given
        initTestQueueReceiverForTopic("streams-json-output");

        // when
        String key = UUID.randomUUID().toString();
        sendMessage(new KafkaStreamsConfig.Test(key, List.of("abc", "def")), key, "streams-json-input");
        sendMessage(new KafkaStreamsConfig.Test(key, List.of("ghi", "jkl")), key, "streams-json-input");

        // then
        Awaitility.await().untilAsserted(() ->
                assertThat(records.stream()
                        .map(ConsumerRecord::value)
                        .filter(Objects::nonNull))
                        .anyMatch(string -> string.contains("\"words\":[\"abc\",\"def\",\"ghi\",\"jkl\"]")));
    }
}
