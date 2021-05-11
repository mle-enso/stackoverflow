package de.mle.stackoverflow.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.DBObject;
import com.mongodb.DBRef;

import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;

import lombok.val;

public class DbRefIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Autowired
    private CapsuleRepository capsuleRepo;
    @Autowired
    private ReferencedEntityRepository refEntityRepo;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void mongoDbRefWithUpdate() {
        // given
        ReferencedEntity reference = new ReferencedEntity();
        reference.setName("ref name");
        val savedReference = refEntityRepo.save(reference);

        Capsule capsule = new Capsule();
        capsule.setName("capsule name");
        capsule.setReferencedEntity(savedReference);

        val savedCapsule = capsuleRepo.save(capsule);

        // when (retrieve initially)
        val retrievedCapsule = capsuleRepo.findById(savedCapsule.getId()).get();

        // then (assert initially)
        assertThat(retrievedCapsule.getReferencedEntity()).isEqualTo(savedReference);
        assertThat(retrievedCapsule.getVersion()).isEqualTo(0);
        assertThat(savedReference.getVersion()).isEqualTo(0);
        assertThat(savedReference.getName()).isEqualTo("ref name");

        // when (update reference)
        savedReference.setName("new ref name");
        refEntityRepo.save(savedReference);

        // then (assert update)
        val freshlyRetrievedCapsule = capsuleRepo.findById(savedCapsule.getId()).get();
        assertThat(freshlyRetrievedCapsule.getReferencedEntity().getName()).isEqualTo("new ref name");
    }

    @Test
    void checkInternalRef() {
        // given
        ReferencedEntity reference = new ReferencedEntity();
        val savedReference = refEntityRepo.save(reference);

        Capsule capsule = new Capsule();
        capsule.setReferencedEntity(savedReference);

        val savedCapsule = capsuleRepo.save(capsule);

        // when
        DBObject result = mongoTemplate.findById(savedCapsule.getId(), DBObject.class, "capsule");

        val dbRef = ((DBRef) result.get("referencedEntity"));
        assertThat(((ObjectId) dbRef.getId()).toHexString()).isEqualTo(savedReference.getId());
        assertThat(dbRef.getCollectionName()).isEqualTo("referencedEntity");
    }
}