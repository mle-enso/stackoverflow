package de.mle.stackoverflow;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class MetricExporter {
    private final MeterRegistry registry;

    @PostConstruct
    public void count() {
        Gauge
                .builder("sample-count", () -> new Random().nextInt(10))
                .description("indicates a random offer count")
                .tags("app", "stackoverflow", "stage", "prod", "host", "http://localhost")
                .register(registry);
    }
}
