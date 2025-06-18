package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.VendorSourceSystemCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorSourceSystemCodeRepository extends JpaRepository<VendorSourceSystemCodeEntity, String> {
}
