package de.mle.stackoverflow.java;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Java14FeaturesTest {
    @Test
    void helpfulNullPointerExceptions() {
        String e = null;
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> e.toString())
                .withMessage("Cannot invoke \"String.toString()\" because \"e\" is null");
    }

    @ParameterizedTest
    @MethodSource("dayAndResult")
    void switchExpressions(String day, boolean isWorkday) {
        assertThat(Java14Features.isWorkday(day)).isEqualTo(isWorkday);
    }

    private static Stream<Arguments> dayAndResult() {
        return Stream.of(
                Arguments.of("MONDAY", true),
                Arguments.of("SATURDAY", false)
        );
    }
}
