package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.AuthorityContactTypeCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityContactTypeCodeRepository extends JpaRepository<AuthorityContactTypeCodeEntity, String> {

}
