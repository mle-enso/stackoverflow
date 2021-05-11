package de.mle.stackoverflow.mongo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ReferencedEntityRepository extends PagingAndSortingRepository<ReferencedEntity, String> {
}
