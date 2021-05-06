package de.mle.stackoverflow.java;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class Java15FeaturesTest {
    @Test
    void textBlock() {
        String query = """
               SELECT "NAME", "LAST_NAME" FROM "TABLE_ONE"
               WHERE "CITY" = 'Seyðisfjörður'
               ORDER BY "NAME";
               """;

        assertThat(query).contains(""" 
                                SELECT "NAME", "LAST_NAME" FROM "TABLE_ONE"
                """.trim());
    }

    @Test
    void patternMatchTypeChecks() {
        Number numberOne = 1;
        if (numberOne instanceof Integer one) {
            double oneAsDouble = one.doubleValue();
            assertThat(oneAsDouble).isEqualTo(1.);
        } else
            fail("Number erroneously not an integer");
    }
}