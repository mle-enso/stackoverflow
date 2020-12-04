package de.mle.stackoverflow.elasticsearch;

import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryIndexIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Autowired
    private ProductRepository repo;
    @Autowired
    private ElasticsearchRestTemplate template;

    @Test
    public void queryRepo() {
        assertThat(template.indexOps(Product.class).exists()).isTrue();

        Product savedProduct = repo.save(new Product(null, "the name", "n/a"));
        Product foundProduct = repo.findByName("the name");

        assertThat(foundProduct).isEqualTo(savedProduct);
    }
}
