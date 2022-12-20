package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * The type Student.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistrictAddress extends BaseAddress implements Serializable {

  private static final long serialVersionUID = 1L;

  private String districtAddressId;

  private String districtId;

}
