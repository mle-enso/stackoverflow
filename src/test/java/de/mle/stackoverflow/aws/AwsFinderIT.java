package de.mle.stackoverflow.aws;

import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class AwsFinderIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Autowired
    private AwsFinder finder;

    @Test
    public void findFolders(){
        assertThat(finder.findFolders("orca-models", "production/producttype-classifier/"))
                .hasSize(3)
                .map(item -> item.split("/")[2])
                .containsExactlyInAnyOrder("2012-12-10", "2021-01-12", "2021-02-08");
    }
}