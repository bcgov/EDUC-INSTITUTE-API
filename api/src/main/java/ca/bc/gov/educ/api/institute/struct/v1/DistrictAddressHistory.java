package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistrictAddressHistory extends BaseAddress implements Serializable {

  private static final long serialVersionUID = 1L;

  private String districtAddressHistoryId;

  private String districtAddressId;

  private String districtId;
}
