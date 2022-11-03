package ca.bc.gov.educ.api.institute.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SuppressWarnings("squid:S1700")
public class SchoolContactTypeCode implements Serializable {

  private static final long serialVersionUID = 6118916290604876032L;

  private String schoolContactTypeCode;

  private String label;

  private String description;

  private String legacyCode;

  private Integer displayOrder;

  private String effectiveDate;

  private String expiryDate;
}
