package de.mle.stackoverflow.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@NoArgsConstructor
public class Capsule {
    @Id
    private String id;
    private String name;
    @Version
    private Long version;
    @DBRef
    private ReferencedEntity referencedEntity;
}
