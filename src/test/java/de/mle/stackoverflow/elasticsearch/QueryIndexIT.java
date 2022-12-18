package de.mle.stackoverflow.elasticsearch;

import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class QueryIndexIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Autowired
    private ProductRepository repo;
    @Autowired
    private ElasticsearchTemplate template;

    @Test
    public void queryRepo() {
        assertThat(template.indexOps(Product.class).exists()).isTrue();

        String name = UUID.randomUUID().toString();
        Product savedProduct = repo.save(new Product(null, name, "n/a"));
        Product foundProduct = repo.findByName(name);

        assertThat(foundProduct).isEqualTo(savedProduct);
    }
}
