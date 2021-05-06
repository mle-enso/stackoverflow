package de.mle.stackoverflow.java;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Java16RecordTest {
    @Test
    void record() {
        var rec = new Java16Record(100, 200);

        assertThat(rec.x()).isEqualTo(100);
        assertThat(rec.y()).isEqualTo(200);
        assertThat(rec.toString()).isEqualTo("Java16Record[x=100, y=200]");
        assertThat(rec).isEqualTo(new Java16Record(100, 200));
    }
}