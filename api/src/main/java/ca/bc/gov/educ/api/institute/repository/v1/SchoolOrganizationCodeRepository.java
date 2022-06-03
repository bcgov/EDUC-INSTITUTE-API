package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.struct.v1.SchoolOrganizationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolOrganizationCodeRepository extends JpaRepository<SchoolOrganizationCode, String> {

}
