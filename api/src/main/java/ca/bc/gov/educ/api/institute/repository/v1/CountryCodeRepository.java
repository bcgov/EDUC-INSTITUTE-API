package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.CountryCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryCodeRepository extends JpaRepository<CountryCodeEntity, String> {

}
