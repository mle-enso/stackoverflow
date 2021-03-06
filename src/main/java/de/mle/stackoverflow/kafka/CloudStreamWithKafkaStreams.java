package de.mle.stackoverflow.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

@Configuration
public class CloudStreamWithKafkaStreams {
    @Bean
    public Function<KStream<Bytes, Words>, KStream<Bytes, WordCount>> process() {
        return input -> input
                .flatMapValues(value -> value.getWords() != null ? value.getWords() : List.of())
                .map((key, value) -> new KeyValue<>(value, value))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                .windowedBy(TimeWindows.of(Duration.ofSeconds(5)))
                .count(Materialized.as("word-counts-state-store"))
                .toStream()
                .map((key, value) -> new KeyValue<>(null, new WordCount(key.key(), value)));
    }
}
