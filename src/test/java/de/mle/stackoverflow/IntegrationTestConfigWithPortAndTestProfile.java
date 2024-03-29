package de.mle.stackoverflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@Slf4j
@AutoConfigureObservability
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegrationTestConfigWithPortAndTestProfile {
    private KafkaMessageListenerContainer<String, String> listenerContainer;

    protected final BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingQueue<>();

    @LocalServerPort
    private int port;

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected WebTestClient webTestClient;
    protected WebClient webClient;

    private KafkaTemplate<String, Object> kafkaTemplate;

    @SneakyThrows
    @BeforeEach
    public void init(RestDocumentationContextProvider restDocumentation) {
        initKafka();
        webClient = WebClient.builder().baseUrl("http://localhost:" + port).build();
        webTestClient = webTestClient.mutate()
                .filter(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withResponseDefaults(prettyPrint())
                        .withRequestDefaults(prettyPrint()))
                .build();
        records.clear();
    }

    @AfterEach
    public void stopListenerContainer() {
        if (listenerContainer != null) {
            listenerContainer.stop();
        }
    }

    private void initKafka() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        DefaultKafkaProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(props);
        producerFactory.setKeySerializer(new StringSerializer());
        JsonSerializer<Object> valueSerializer = new JsonSerializer<>(objectMapper);
        valueSerializer.setAddTypeInfo(false);
        producerFactory.setValueSerializer(valueSerializer);

        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    protected void initTestQueueReceiverForTopic(String topic) {
        initTestQueueReceiverForTopicAndBroker(topic, "localhost:9092");
    }

    protected void initTestQueueReceiverForTopicAndBroker(String topic, String broker) {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "json");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        var cf = new DefaultKafkaConsumerFactory<String, String>(props);
        ContainerProperties containerProps = new ContainerProperties(topic);
        containerProps.setMissingTopicsFatal(false);
        records.clear();
        listenerContainer = new KafkaMessageListenerContainer<>(cf, containerProps);
        listenerContainer.setupMessageListener(new MessageRecorder());
        listenerContainer.setBeanName("beanForTopic-" + topic);
        listenerContainer.start();
    }

    private class MessageRecorder implements MessageListener<String, String> {
        @Override
        public void onMessage(ConsumerRecord<String, String> message) {
            records.add(message);
        }
    }

    @SneakyThrows
    protected ConsumerRecord<String, String> waitForMessageWithMatchingContent(Predicate<String> contentToAssert) {
        ConsumerRecord<String, String> poll;
        boolean isCorrectReviewFound = false;
        do {
            poll = records.poll(5, TimeUnit.SECONDS);
            if (poll != null)
                log.info("Poll… {}", poll.value());
            if (poll != null && poll.value() != null && contentToAssert.test(poll.value())) {
                isCorrectReviewFound = true;
            }
        } while (!isCorrectReviewFound);
        return poll;
    }

    protected void waitForDeleteMessageWithKey(String key) {
        waitForMessageWithKeyAndContent(key::equals, Objects::isNull);
    }

    protected ConsumerRecord<String, String> waitForNonDeleteMessageWithKey(String key) {
        return waitForMessageWithKeyAndContent(key::equals, Objects::nonNull);
    }

    protected ConsumerRecord<String, String> waitForMessageWithKeyAndContent(Predicate<String> keyMatcher, Predicate<String> contentMatcher) {
        AtomicReference<ConsumerRecord<String, String>> result = new AtomicReference<>();
        Awaitility.await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(Duration.ofMillis(5))
                .until(() -> {
                    ConsumerRecord<String, String> poll = records.poll();
                    if (poll != null && keyMatcher.test(poll.key()) && contentMatcher.test(poll.value())) {
                        result.set(poll);
                        return true;
                    }
                    return false;
                });
        return result.get();
    }

    @SneakyThrows
    protected SendResult<String, Object> sendMessage(Object msg, String key, String topic) {
        return kafkaTemplate.send(topic, key, msg).get(5, TimeUnit.SECONDS);
    }
}
