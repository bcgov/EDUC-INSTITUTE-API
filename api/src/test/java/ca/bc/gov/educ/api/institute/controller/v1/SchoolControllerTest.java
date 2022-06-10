package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.mapper.v1.CodeTableMapper;
import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.*;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = InstituteApiResourceApplication.class)
@AutoConfigureMockMvc
public class SchoolControllerTest {

  private static final CodeTableMapper mapper = CodeTableMapper.mapper;
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  CodeTableAPIController controller;

  @Autowired
  SchoolRepository schoolRepository;

  @Autowired
  SchoolHistoryRepository schoolHistoryRepository;

  @Autowired
  CodeTableService codeTableService;

  @Autowired
  SchoolCategoryCodeRepository schoolCategoryCodeRepository;

  @Autowired
  SchoolOrganizationCodeRepository schoolOrganizationCodeRepository;

  @Autowired
  FacilityTypeCodeRepository facilityTypeCodeRepository;

  @Autowired
  ContactTypeCodeRepository contactTypeCodeRepository;

  @Autowired
  ContactRepository contactRepository;

  @Autowired
  AddressRepository addressRepository;

  @Autowired
  NoteRepository noteRepository;

  @Autowired
  AddressTypeCodeRepository addressTypeCodeRepository;

  @Autowired
  ProvinceCodeRepository provinceCodeRepository;

  @Autowired
  CountryCodeRepository countryCodeRepository;

  @Autowired
  SchoolGradeCodeRepository schoolGradeCodeRepository;


  @Autowired
  NeighborhoodLearningTypeCodeRepository neighborhoodLearningTypeCodeRepository;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Before
  public void before(){
    this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData());
    this.schoolOrganizationCodeRepository.save(this.createSchoolOrganizationCodeData());
    this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData());
    this.contactTypeCodeRepository.save(this.createContactTypeCodeData());
    this.addressTypeCodeRepository.save(this.createAddressTypeCodeData());
    this.countryCodeRepository.save(this.createCountryCodeData());
    this.provinceCodeRepository.save(this.createProvinceCodeData());
    this.schoolGradeCodeRepository.save(this.createSchoolGradeCodeData());
    this.neighborhoodLearningTypeCodeRepository.save(this.createNeighborhoodLearningTypeCodeData());
  }

  /**
   * need to delete the records to make it working in unit tests assertion, else the records will keep growing and assertions will fail.
   */
  @After
  public void after() {
    this.addressRepository.deleteAll();
    this.contactRepository.deleteAll();
    this.noteRepository.deleteAll();
    this.schoolRepository.deleteAll();
    this.schoolHistoryRepository.deleteAll();
    this.schoolGradeCodeRepository.deleteAll();
    this.neighborhoodLearningTypeCodeRepository.deleteAll();
    this.schoolCategoryCodeRepository.deleteAll();
    this.schoolOrganizationCodeRepository.deleteAll();
    this.facilityTypeCodeRepository.deleteAll();
  }

  @Test
  public void testAllSchools_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].schoolId")
        .value(entity.getSchoolId().toString()));
  }

  @Test
  public void testRetrieveSchool_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.schoolId")
        .value(entity.getSchoolId().toString()));
  }

  @Test
  public void testRetrieveSchool_GivenInvalidID_ShouldReturnStatusNotFound() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + UUID.randomUUID()).with(mockAuthority))
      .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void testRetrieveSchoolHistory_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    final SchoolHistoryEntity historyEntity = this.schoolHistoryRepository.save(this.createHistorySchoolData(entity.getSchoolId()));
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId() + "/history").with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].schoolHistoryId")
        .value(historyEntity.getSchoolHistoryId().toString()));
  }

  @Test
  public void testRetrieveSchoolHistory_GivenInvalidID_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/abc/history").with(mockAuthority))
      .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void testRetrieveSchool_GivenInvalidID_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/abc").with(mockAuthority))
      .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void testDeleteSchool_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var school = this.createSchoolData();
    var entity = this.schoolRepository.save(school);

    this.mockMvc.perform(delete(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedEntity = this.schoolRepository.findById(entity.getSchoolId());
    Assert.assertTrue(deletedEntity.isEmpty());
  }

  @Test
  public void testUpdateSchool_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var school = this.createSchoolData();
    var entity = this.schoolRepository.save(school);
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(entity.getDisplayName().toUpperCase()));
  }

  @Test
  public void testAddSchoolGrade_GivenValidPayload_ShouldReturnStatusOk() throws Exception {
    final var school = this.createSchoolData();
    var entity = this.schoolRepository.save(school);
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);
    entity.getGrades().add(createSchoolGradeData(entity));

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.grades[0].schoolGradeCode").value("01"));
  }

  @Test
  public void testAddSchoolNeighborhoodLearning_GivenValidPayload_ShouldReturnStatusOk() throws Exception {
    final var school = this.createSchoolData();
    var entity = this.schoolRepository.save(school);
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);
    entity.getNeighborhoodLearning().add(createNeighborhoodLearningData(entity));

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.neighborhoodLearning[0].neighborhoodLearningTypeCode").value("COMM_USE"));
  }

  @Test
  public void testCreateSchool_GivenValidPayload_ShouldReturnStatusOK() throws Exception {
    final var school = this.createSchoolData();
    school.setCreateDate(null);
    school.setUpdateDate(null);
    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(school))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(school.getDisplayName().toUpperCase()));
  }

  @Test
  public void testCreateSchool_GivenInvalidPayload_ShouldReturnStatusBadRequest() throws Exception {
    final var school = this.createSchoolData();
    school.setSchoolCategoryCode("ABCD");
    school.setCreateDate(null);
    school.setUpdateDate(null);
    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(school))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateSchoolContact_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final SchoolEntity schoolEntity = this.schoolRepository.save(this.createSchoolData());
    ContactEntity contactEntity = createContactData(schoolEntity);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_CONTACT"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(contactEntity.getLastName().toUpperCase()));
  }

  @Test
  public void testDeleteSchoolContact_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    ContactEntity contactEntity = createContactData(schoolEntity);
    var contact = this.contactRepository.save(contactEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact/" + contact.getContactId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(schoolEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_SCHOOL_CONTACT"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedContact = this.contactRepository.findById(contact.getContactId());
    Assert.assertTrue(deletedContact.isEmpty());
  }

  @Test
  public void testRetrieveSchoolContact_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_CONTACT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    ContactEntity contactEntity = createContactData(schoolEntity);
    var contact = this.contactRepository.save(contactEntity);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact/" + contact.getContactId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.contactId")
        .value(contact.getContactId().toString()));
  }

  @Test
  public void testRetrieveSchoolContact_GivenInvalidID_ShouldReturnStatusNotFound() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_CONTACT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);

    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact/" + UUID.randomUUID()).with(mockAuthority))
      .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateSchoolContact_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    ContactEntity contactEntity = createContactData(schoolEntity);
    var contact = this.contactRepository.save(contactEntity);
    contact.setFirstName("pete");

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact/" + contact.getContactId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contact))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_CONTACT"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(contact.getFirstName().toUpperCase()));
  }

  @Test
  public void testCreateSchoolAddress_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final SchoolEntity schoolEntity = this.schoolRepository.save(this.createSchoolData());
    AddressEntity addressEntity = createAddressData(schoolEntity);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/address")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(addressEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_ADDRESS"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(addressEntity.getCity().toUpperCase()));
  }

  @Test
  public void testDeleteSchoolAddress_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    AddressEntity addressEntity = createAddressData(schoolEntity);
    var address = this.addressRepository.save(addressEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/address/" + address.getAddressId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(schoolEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_SCHOOL_ADDRESS"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedAddress = this.addressRepository.findById(address.getAddressId());
    Assert.assertTrue(deletedAddress.isEmpty());
  }

  @Test
  public void testRetrieveSchoolAddress_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_ADDRESS";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    AddressEntity addressEntity = createAddressData(schoolEntity);
    var address = this.addressRepository.save(addressEntity);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/address/" + address.getAddressId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.addressId")
        .value(address.getAddressId().toString()));
  }

  @Test
  public void testRetrieveSchoolAddress_GivenInvalidID_ShouldReturnStatusNotFound() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_ADDRESS";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);

    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/address/" + UUID.randomUUID()).with(mockAuthority))
      .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateSchoolAddress_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    AddressEntity addressEntity = createAddressData(schoolEntity);
    var address = this.addressRepository.save(addressEntity);
    address.setCity("southshore");

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/address/" + address.getAddressId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(address))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_ADDRESS"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(address.getCity().toUpperCase()));
  }

  @Test
  public void testCreateSchoolNote_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final SchoolEntity schoolEntity = this.schoolRepository.save(this.createSchoolData());
    NoteEntity noteEntity = createNoteData(schoolEntity);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/note")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(noteEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_NOTE"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(noteEntity.getContent()));
  }

  @Test
  public void testDeleteSchoolNote_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    NoteEntity noteEntity = createNoteData(schoolEntity);
    var note = this.noteRepository.save(noteEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/note/" + note.getNoteId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(schoolEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_SCHOOL_NOTE"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedNote = this.noteRepository.findById(note.getNoteId());
    Assert.assertTrue(deletedNote.isEmpty());
  }

  @Test
  public void testRetrieveSchoolNote_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_NOTE";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    NoteEntity noteEntity = createNoteData(schoolEntity);
    var note = this.noteRepository.save(noteEntity);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/note/" + note.getNoteId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.noteId")
        .value(note.getNoteId().toString()));
  }

  @Test
  public void testRetrieveSchoolNote_GivenInvalidID_ShouldReturnStatusNotFound() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_NOTE";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/note/" + UUID.randomUUID()).with(mockAuthority))
      .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateSchoolNote_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    NoteEntity noteEntity = createNoteData(schoolEntity);
    var note = this.noteRepository.save(noteEntity);
    note.setContent("southshore");

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/note/" + note.getNoteId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(note))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_NOTE"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(note.getContent()));
  }

  private SchoolEntity createSchoolData() {
    return SchoolEntity.builder().schoolNumber("003").displayName("School Name").openedDate(LocalDateTime.now().minusDays(1)).schoolCategoryCode("PUB_SCHL")
      .schoolOrganizationCode("TWO_SEM").facilityTypeCode("STAND_SCHL").website("abc@sd99.edu").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolHistoryEntity createHistorySchoolData(UUID schoolId) {
    return SchoolHistoryEntity.builder().schoolId(schoolId).schoolNumber("003").displayName("School Name").openedDate(LocalDateTime.now().minusDays(1)).schoolCategoryCode("PUB_SCHL")
      .schoolOrganizationCode("TWO_SEM").facilityTypeCode("STAND_SCHL").website("abc@sd99.edu").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  public static String asJsonString(final Object obj) {
    try {
      ObjectMapper om = new ObjectMapper();
      om.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      return om.writeValueAsString(obj);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  private SchoolOrganizationCodeEntity createSchoolOrganizationCodeData() {
    return SchoolOrganizationCodeEntity.builder().schoolOrganizationCode("TWO_SEM").description("Two Semesters")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Two Semesters").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolCategoryCodeEntity createSchoolCategoryCodeData() {
    return SchoolCategoryCodeEntity.builder().schoolCategoryCode("PUB_SCHL").description("Public School")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Public School").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private FacilityTypeCodeEntity createFacilityTypeCodeData() {
    return FacilityTypeCodeEntity.builder().facilityTypeCode("STAND_SCHL").description("Standard School")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Standard School").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private ContactEntity createContactData(SchoolEntity entity) {
    return ContactEntity.builder().schoolEntity(entity).contactTypeCode("PRINCIPAL").firstName("John").lastName("Wayne").createUser("TEST").updateUser("TEST").build();
  }

  private SchoolGradeEntity createSchoolGradeData(SchoolEntity entity) {
    return SchoolGradeEntity.builder().schoolEntity(entity).schoolGradeCode("01").createUser("TEST").updateUser("TEST").build();
  }

  private NeighborhoodLearningEntity createNeighborhoodLearningData(SchoolEntity entity) {
    return NeighborhoodLearningEntity.builder().schoolEntity(entity).neighborhoodLearningTypeCode("COMM_USE").createUser("TEST").updateUser("TEST").build();
  }

  private AddressEntity createAddressData(SchoolEntity entity) {
    return AddressEntity.builder().schoolEntity(entity).addressTypeCode("MAILING").addressLine1("123 This Street").city("Compton")
      .provinceCode("BC").countryCode("CAN").postal("V1B9H2").createUser("TEST").updateUser("TEST").build();
  }

  private NoteEntity createNoteData(SchoolEntity entity) {
    return NoteEntity.builder().schoolEntity(entity).content("This is a note.").createUser("TEST").updateUser("TEST").build();
  }

  private ContactTypeCodeEntity createContactTypeCodeData() {
    return ContactTypeCodeEntity.builder().contactTypeCode("PRINCIPAL").description("School Principal")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Principal").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private AddressTypeCodeEntity createAddressTypeCodeData() {
    return AddressTypeCodeEntity.builder().addressTypeCode("MAILING").description("Mailing Address")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Mailing Address").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private ProvinceCodeEntity createProvinceCodeData() {
    return ProvinceCodeEntity.builder().provinceCode("BC").description("British Columbia")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("British Columbia").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private CountryCodeEntity createCountryCodeData() {
    return CountryCodeEntity.builder().countryCode("CAN").description("Canada")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Canada").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolGradeCodeEntity createSchoolGradeCodeData() {
    return SchoolGradeCodeEntity.builder().schoolGradeCode("01").description("First Grade")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("First").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private NeighborhoodLearningTypeCodeEntity createNeighborhoodLearningTypeCodeData() {
    return NeighborhoodLearningTypeCodeEntity.builder().neighborhoodLearningTypeCode("COMM_USE").description("Community Use")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Community Use").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }
}


