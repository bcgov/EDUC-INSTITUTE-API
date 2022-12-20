package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorityAddressHistory extends BaseAddress implements Serializable {

  private static final long serialVersionUID = 1L;

  private String independentAuthorityAddressHistoryId;

  private String independentAuthorityId;
}
