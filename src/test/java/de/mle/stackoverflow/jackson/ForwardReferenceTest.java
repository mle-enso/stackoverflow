package de.mle.stackoverflow.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ForwardReferenceTest {
    @Test
    void forwardReference() throws JsonProcessingException {
        String json = "{\"people\": [\n" +
                "    {\n" +
                "        \"personId\": 1,\n" +
                "        \"name\": \"An\",\n" +
                "        \"friends\": [2]\n" +
                "    },\n" +
                "    {\n" +
                "        \"personId\": 2,\n" +
                "        \"name\": \"Bob\",\n" +
                "        \"friends\": [1]\n" +
                "    }\n" +
                "]}";

        Map<String, List<People>> people = new ObjectMapper().readValue(json, new TypeReference<Map<String, List<People>>>() {
        });
        assertThat(people.get("people").get(1).getFriends().get(0).getPersonId()).isEqualTo(1);
    }
}
