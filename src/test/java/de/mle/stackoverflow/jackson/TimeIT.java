package de.mle.stackoverflow.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void serializeInstant() throws JsonProcessingException {
        Time value = new Time(Instant.ofEpochMilli(1569238532033l));
        String time = mapper.writeValueAsString(value);
        assertThat(time).isEqualTo("{\"instant\":\"2019-09-23T11:35:32.033Z\"}");
    }
}
