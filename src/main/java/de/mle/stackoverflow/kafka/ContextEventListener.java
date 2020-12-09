package de.mle.stackoverflow.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.stream.binder.kafka.streams.KafkaStreamsRegistry;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ContextEventListener {
    @Autowired
    private KafkaStreamsRegistry kafkaStreamsRegistry;

    @EventListener(ApplicationStartedEvent.class)
    public void handleContextStartedEvent(ApplicationStartedEvent event) {
        if (kafkaStreamsRegistry.streamsBuilderFactoryBeans().isEmpty())
            return;

        String topology = kafkaStreamsRegistry.streamsBuilderFactoryBeans()
                .stream().map(bean -> bean.getTopology().describe().toString())
                .collect(Collectors.joining());

        log.info("Current topology:\n{}", topology);
    }
}
