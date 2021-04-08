package de.mle.stackoverflow.reactive;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import reactor.blockhound.BlockingOperationError;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThatCode;

@Slf4j
public class BlockHoundTest {
    @Test
    public void blockHoundWorks() {
        ThrowableAssert.ThrowingCallable blockingInside = () -> {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
        };
        assertThatCode(blockingInside).getCause().isInstanceOf(BlockingOperationError.class);
    }
}
