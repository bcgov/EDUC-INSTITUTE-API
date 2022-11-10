package ca.bc.gov.educ.api.institute.constants.v1;

public final class URL {
  public static final String SCHOOL_BY_ID = "/{schoolId}";
  public static final String CATEGORY_CODES = "/category-codes";
  public static final String GRADE_CODES = "/grade-codes";
  public static final String FACILITY_TYPE_CODES = "/facility-codes";
  public static final String ORGANIZATION_CODES = "/organization-codes";
  public static final String NEIGHBORHOOD_LEARNING_CODES = "/neighborhood-learning-codes";
  public static final String ADDRESS_TYPE_CODES = "/address-type-codes";
  public static final String AUTHORITY_GROUP_CODES = "/authority-group-codes";
  public static final String AUTHORITY_TYPE_CODES = "/authority-type-codes";
  public static final String DISTRICT_CONTACT_TYPE_CODES = "/district-contact-type-codes";

  public static final String SCHOOL_CONTACT_TYPE_CODES = "/school-contact-type-codes";

  public static final String AUTHORITY_CONTACT_TYPE_CODES = "/authority-contact-type-codes";
  public static final String PROVINCE_CODES = "/province-codes";

  public static final String COUNTRY_CODES = "/country-codes";
  public static final String DISTRICT_REGION_CODES = "/district-region-codes";
  public static final String DISTRICT_STATUS_CODES = "/district-status-codes";

  private URL(){
  }

  public static final String BASE_URL="/api/v1/institute";
  public static final String BASE_URL_DISTRICT="/api/v1/institute/district";
  public static final String BASE_URL_SCHOOL="/api/v1/institute/school";
  public static final String BASE_URL_SCHOOL_HISTORY="/api/v1/institute/school/history";
  public static final String BASE_URL_AUTHORITY="/api/v1/institute/authority";
}
