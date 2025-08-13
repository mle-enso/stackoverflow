package de.mle.stackoverflow;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class PrometheusEndpointIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Test
    public void queryPrometheusEndpoint() {
        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(
                () -> {
                    String body = webClient.mutate().exchangeStrategies(ExchangeStrategies.builder()
                                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build()).build()
                            .get()
                            .uri("/actuator/prometheus")
                            .retrieve()
                            .toEntity(String.class).block().getBody();

                    assertThat(body).contains("sample_count");
                    IntStream.range(1, 15)
                            .forEach(i -> assertThat(body).contains("attributes_size_attributes_bucket{le=\"" + i + ".0\"}"));
                    assertThat(body).contains("attributes_size_attributes_bucket{le=\"16.0\"}");
                    assertThat(body).contains("attributes_size_attributes_bucket{le=\"21.0\"}");
                    assertThat(body).contains("attributes_size_attributes_bucket{le=\"26.0\"}");
                    assertThat(body).contains("attributes_size_attributes_bucket{le=\"30.0\"}");
                    assertThat(body).contains("attributes_size_attributes_bucket{le=\"+Inf\"}");
                    assertThat(body).contains("attributes_size_attributes_max", "attributes_size_attributes_sum", "attributes_size_attributes_count");
                }
        );
    }
}
