package de.mle.stackoverflow.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@NoArgsConstructor
public class ReferencedEntity {
    @Id
    private String id;
    @Version
    private Long version;
    private String name;
}