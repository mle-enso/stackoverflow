package de.mle.stackoverflow.kafka;

import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Test
    void read() {
        // given
        initTestQueueReceiverForTopic("streams-json-output");

        // when
        String key = UUID.randomUUID().toString();
        sendMessage(new KafkaStreamsConfig.Test(key, List.of("abc", "def")), key, "streams-json-input");
        sendMessage(new KafkaStreamsConfig.Test(key, List.of("ghi", "jkl")), key, "streams-json-input");

        // then
        ConsumerRecord<String, String> record = waitForMessageWithMatchingContent(o -> o != null);
        assertThat(record.value()).contains("\"words\":[\"abc\",\"def\",\"ghi\",\"jkl\"]");
    }
}
