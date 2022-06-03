package ca.bc.gov.educ.api.institute.repository.v1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface School repository.
 */
@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, Mincode> {

}
