package ca.bc.gov.educ.api.institute.model.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The type School entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SCHOOL")
public class SchoolEntity {
  @EmbeddedId
  private Mincode mincode;

  @Column(name = "SC_ADDRESS_LINE_1")
  private String scAddressLine1;

  @Column(name = "SC_ADDRESS_LINE_2")
  private String scAddressLine2;

  @Column(name = "SC_CITY")
  private String scCity;

  @Column(name = "SC_PROVINCE_CODE")
  private String scProvinceCode;

  @Column(name = "SC_COUNTRY_CODE")
  private String scCountryCode;

  @Column(name = "SC_POSTAL_CODE")
  private String scPostalCode;

  @Column(name = "SC_FAX_NUMBER")
  private String scFaxNumber;

  @Column(name = "SC_PHONE_NUMBER")
  private String scPhoneNumber;

  @Column(name = "SC_E_MAIL_ID")
  private String scEMailId;

  @Column(name = "FACILITY_TYPE_CODE")
  private String facilityTypeCode;

  @Column(name = "SCHOOL_NAME")
  private String schoolName;

  @Column(name = "SCHOOL_TYPE_CODE")
  private String schoolTypeCode;

  @Column(name = "SCHOOL_ORGANIZATION_CODE")
  private String schoolOrganizationCode;

  @Column(name = "SCHOOL_CATEGORY_CODE")
  private String schoolCategoryCode;

  @Column(name = "PR_GIVEN_NAME")
  private String prGivenName;

  @Column(name = "PR_SURNAME")
  private String prSurname;

  @Column(name = "PR_MIDDLE_NAME")
  private String prMiddleName;

  @Column(name = "PR_TITLE_CODE")
  private String prTitleCode;

  @Column(name = "NUMBER_OF_DIVISIONS")
  private Long numberOfDivisions;

  @Column(name = "NUMBER_OF_SEC_FTE_TEACHERS")
  private Long numberOfSecFteTeachers;

  @Column(name = "NUMBER_OF_ELM_FTE_TEACHERS")
  private Long numberOfElmFteTeachers;

  @Column(name = "TTBL_ELEM_INSTR_MINUTES")
  private Long ttblElemInstrMinutes;

  @Column(name = "SCHOOL_STATUS_CODE")
  private String schoolStatusCode;

  @Column(name = "ENROL_HEADCOUNT_1523")
  private Long enrolHeadcount1523;

  @Column(name = "ENROL_HEADCOUNT_1701")
  private Long enrolHeadcount1701;

  @Column(name = "GRADE_01_IND")
  private String grade01Ind;

  @Column(name = "GRADE_29_IND")
  private String grade29Ind;

  @Column(name = "GRADE_04_IND")
  private String grade04Ind;

  @Column(name = "GRADE_05_IND")
  private String grade05Ind;

  @Column(name = "GRADE_06_IND")
  private String grade06Ind;

  @Column(name = "GRADE_07_IND")
  private String grade07Ind;

  @Column(name = "GRADE_08_IND")
  private String grade08Ind;

  @Column(name = "GRADE_09_IND")
  private String grade09Ind;

  @Column(name = "GRADE_10_IND")
  private String grade10Ind;

  @Column(name = "GRADE_11_IND")
  private String grade11Ind;

  @Column(name = "GRADE_12_IND")
  private String grade12Ind;

  @Column(name = "GRADE_79_IND")
  private String grade79Ind;

  @Column(name = "GRADE_89_IND")
  private String grade89Ind;

  @Column(name = "OPENED_DATE")
  private String openedDate;

  @Column(name = "CLOSED_DATE")
  private String closedDate;

  @Column(name = "AUTH_NUMBER")
  private String authNumber;

  @Column(name = "CREATE_DATE")
  private Long createDate;

  @Column(name = "CREATE_TIME")
  private Long createTime;

  @Column(name = "CREATE_USERNAME")
  private String createUsername;

  @Column(name = "EDIT_DATE")
  private Long editDate;

  @Column(name = "EDIT_TIME")
  private Long editTime;

  @Column(name = "EDIT_USERNAME")
  private String editUsername;

  @Column(name = "ELEM_TEACHERS_HC")
  private Long elemTeachersHc;

  @Column(name = "SEC_TEACHERS_HC")
  private Long secTeachersHc;

  /**
   * The 'comment' is a reserved word in Oracle. Using it will cause the SQLGrammarException
   * @Column(name = "COMMENT")
   * private String comment;
   */

  @Column(name = "GRADE_KH_IND")
  private String gradeKhInd;

  @Column(name = "GRADE_KF_IND")
  private String gradeKfInd;

  @Column(name = "GRADE_02_IND")
  private String grade02Ind;

  @Column(name = "GRADE_03_IND")
  private String grade03Ind;

  @Column(name = "GRADE_EU_IND")
  private String gradeEuInd;

  @Column(name = "GRADE_SU_IND")
  private String gradeSuInd;

  @Column(name = "GRADE_HS_IND")
  private String gradeHsInd;

  @Column(name = "CONTED_FUND_FLAG")
  private String contedFundFlag;

  @Column(name = "ELEM_FTE_CLASSROOM")
  private Long elemFteClassroom;

  @Column(name = "ELEM_FTE_SUPPORT")
  private Long elemFteSupport;

  @Column(name = "ELEM_FTE_ADMIN")
  private Long elemFteAdmin;

  @Column(name = "SEC_FTE_CLASSROOM")
  private Long secFteClassroom;

  @Column(name = "SEC_FTE_SUPPORT")
  private Long secFteSupport;

  @Column(name = "SEC_FTE_ADMIN")
  private Long secFteAdmin;

  @Column(name = "PHYS_ADDRESS_LINE_1")
  private String physAddressLine1;

  @Column(name = "PHYS_ADDRESS_LINE_2")
  private String physAddressLine2;

  @Column(name = "PHYS_CITY")
  private String physCity;

  @Column(name = "PHYS_PROVINCE_CODE")
  private String physProvinceCode;

  @Column(name = "PHYS_COUNTRY_CODE")
  private String physCountryCode;

  @Column(name = "PHYS_POSTAL_CODE")
  private String physPostalCode;

  @Column(name = "EDUC_METHOD_CLASS_CNT")
  private Long educMethodClassCnt;

  @Column(name = "EDUC_METHOD_DEL_CNT")
  private Long educMethodDelCnt;

  @Column(name = "EDUC_METHOD_BOTH_CNT")
  private Long educMethodBothCnt;

  @Column(name = "NEW_DISTNO")
  private String newDistno;

  @Column(name = "NEW_SCHLNO")
  private String newSchlno;

  @Column(name = "DATE_OPENED")
  private LocalDateTime dateOpened;

  @Column(name = "DATE_CLOSED")
  private LocalDateTime dateClosed;

  @Column(name = "ASSET_NUMBER")
  private Long assetNumber;

  @Column(name = "ASSET_ASSIGNED_BY")
  private String assetAssignedBy;

  @Column(name = "ASSET_ASSIGNED_DATE")
  private LocalDateTime assetAssignedDate;

  @Column(name = "ASSET_CHANGED_BY")
  private String assetChangedBy;

  @Column(name = "ASSET_CHANGED_DATE")
  private LocalDateTime assetChangedDate;

  @Column(name = "RESTRICT_FUNDING")
  private String restrictFunding;

  @Column(name = "GRADE_GA_IND")
  private String gradeGaInd;

  @Column(name = "NLC_EARLY_LEARNING_FLAG")
  private String nlcEarlyLearningFlag;

  @Column(name = "NLC_AFTER_SCHOOL_PROGRAM_FLAG")
  private String nlcAfterSchoolProgramFlag;

  @Column(name = "NLC_CONTINUING_ED_FLAG")
  private String nlcContinuingEdFlag;

  @Column(name = "NLC_SENIORS_FLAG")
  private String nlcSeniorsFlag;

  @Column(name = "NLC_SPORT_AND_REC_FLAG")
  private String nlcSportAndRecFlag;

  @Column(name = "NLC_COMMUNITY_USE_FLAG")
  private String nlcCommunityUseFlag;

  @Column(name = "NLC_INTEGRATED_SERVICES_FLAG")
  private String nlcIntegratedServicesFlag;
}
