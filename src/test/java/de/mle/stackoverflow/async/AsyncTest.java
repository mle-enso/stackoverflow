package de.mle.stackoverflow.async;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

public class AsyncTest {
    private AsyncService service = new AsyncService();

    @Test
    public void call() throws InterruptedException {
        List<String> list = service.toCall();
        assertThat(list).hasSize(2);
        assertThat(list).containsExactly("a", "b");
    }
}
