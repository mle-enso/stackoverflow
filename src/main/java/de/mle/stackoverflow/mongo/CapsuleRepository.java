package de.mle.stackoverflow.mongo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CapsuleRepository extends PagingAndSortingRepository<Capsule, String>, CrudRepository<Capsule, String> {
}
