package de.mle.stackoverflow.reactive;

import com.google.auto.service.AutoService;
import org.assertj.core.api.Assertions;
import reactor.blockhound.BlockHound.Builder;
import reactor.blockhound.integration.BlockHoundIntegration;

import java.io.FileOutputStream;
import java.io.PrintStream;

@AutoService(BlockHoundIntegration.class)
public class BlockHoundCustomizerSpi implements BlockHoundIntegration {
    @Override
    public void applyTo(Builder builder) {
        builder
                .allowBlockingCallsInside(PrintStream.class.getName(), "write")
                .allowBlockingCallsInside(MonoAndFlux.class.getName(), "write")
                .allowBlockingCallsInside(MonoAndFlux.class.getName(), "write");
        /* enable only for debugging purposes
                .blockingMethodCallback(it -> {
                    new Exception(it.toString()).printStackTrace();
                });
         */
    }
}