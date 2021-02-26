package de.mle.stackoverflow.async;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AsyncTest {

    private AsyncService service = new AsyncService();

    @Test
    public void call() throws InterruptedException {
        List<String> list = service.toCall();
        assertThat(list).hasSize(2);
        assertThat(list).containsExactly("a", "b");
    }
}
