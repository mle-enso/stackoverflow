package de.mle.stackoverflow.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;

import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;

@DirtiesContext
@Import(TestChannelBinderConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.cloud.stream.function.definition=concatHash;concatStar")
public class CloudStreamWithKafkaLowLevelIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Autowired
    private InputDestination inputDestination;
    @Autowired
    private OutputDestination outputDestination;

    @Test
    public void testMultipleFunctions() {
        Message<byte[]> inputMessage = MessageBuilder.withPayload("Hello".getBytes()).build();
        inputDestination.send(inputMessage, "concatHash-in-0");
        inputDestination.send(inputMessage, "concatStar-in-0");

        Message<byte[]> outputMessage = outputDestination.receive(0, "concatHash-out-0");
        assertThat(outputMessage.getPayload()).isEqualTo("Hello#".getBytes());

        outputMessage = outputDestination.receive(0, "concatStar-out-0");
        assertThat(outputMessage.getPayload()).isEqualTo("Hello*".getBytes());
    }
}
