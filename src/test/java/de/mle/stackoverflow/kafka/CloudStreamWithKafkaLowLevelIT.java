package de.mle.stackoverflow.kafka;

import de.mle.stackoverflow.StackoverflowApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import static org.assertj.core.api.Assertions.assertThat;

public class CloudStreamWithKafkaLowLevelIT {
    @Test
    public void testMultipleFunctions() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        StackoverflowApplication.class))
                .web(WebApplicationType.SERVLET)
                .run("--spring.cloud.stream.function.definition=concatHash;concatStar")) {
            context.getBean(InputDestination.class);

            InputDestination inputDestination = context.getBean(InputDestination.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> inputMessage = MessageBuilder.withPayload("Hello".getBytes()).build();
            inputDestination.send(inputMessage, "concatHash-in-0");
            inputDestination.send(inputMessage, "concatStar-in-0");

            Message<byte[]> outputMessage = outputDestination.receive(0, "concatHash-out-0");
            assertThat(outputMessage.getPayload()).isEqualTo("Hello#".getBytes());

            outputMessage = outputDestination.receive(0, "concatStar-out-0");
            assertThat(outputMessage.getPayload()).isEqualTo("Hello*".getBytes());
        }
    }
}
