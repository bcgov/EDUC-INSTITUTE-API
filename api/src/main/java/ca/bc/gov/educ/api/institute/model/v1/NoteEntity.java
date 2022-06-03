package ca.bc.gov.educ.api.institute.model.v1;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "NOTE")
public class NoteEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "note_id")
  private UUID noteId;
  @Basic
  @Column(name = "school_id")
  private UUID schoolId;
  @Basic
  @Column(name = "district_id")
  private UUID districtId;
  @Basic
  @Column(name = "independent_authority_id")
  private UUID independentAuthorityId;
  @Basic
  @Column(name = "content")
  private String content;
  @Column(name = "CREATE_USER", updatable = false)
  private String createUser;
  @PastOrPresent
  @Column(name = "CREATE_DATE", updatable = false)
  private LocalDateTime createDate;
  @Column(name = "update_user")
  private String updateUser;
  @PastOrPresent
  @Column(name = "update_date")
  private LocalDateTime updateDate;
}
