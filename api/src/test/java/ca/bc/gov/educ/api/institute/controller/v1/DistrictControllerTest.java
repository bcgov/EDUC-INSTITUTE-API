package ca.bc.gov.educ.api.institute.controller.v1;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.mapper.v1.CodeTableMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.DistrictMapper;
import ca.bc.gov.educ.api.institute.model.v1.AddressTypeCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.CountryCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictAddressEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictContactEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictContactTypeCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictHistoryEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictRegionCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictStatusCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.NoteEntity;
import ca.bc.gov.educ.api.institute.model.v1.ProvinceCodeEntity;
import ca.bc.gov.educ.api.institute.repository.v1.AddressTypeCodeRepository;
import ca.bc.gov.educ.api.institute.repository.v1.CountryCodeRepository;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictAddressRepository;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictContactRepository;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictContactTypeCodeRepository;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictHistoryRepository;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictRegionCodeRepository;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictRepository;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictStatusCodeRepository;
import ca.bc.gov.educ.api.institute.repository.v1.NoteRepository;
import ca.bc.gov.educ.api.institute.repository.v1.ProvinceCodeRepository;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = {InstituteApiResourceApplication.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DistrictControllerTest {

  private static final CodeTableMapper mapper = CodeTableMapper.mapper;
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  CodeTableAPIController controller;

  @Autowired
  DistrictRepository districtRepository;

  @Autowired
  DistrictHistoryRepository districtHistoryRepository;

  @Autowired
  CodeTableService codeTableService;

  @Autowired
  DistrictRegionCodeRepository districtRegionCodeRepository;

  @Autowired
  DistrictStatusCodeRepository districtStatusCodeRepository;

  @Autowired
  DistrictContactTypeCodeRepository districtContactTypeCodeRepository;

  @Autowired
  DistrictContactRepository districtContactRepository;

  @Autowired
  DistrictAddressRepository addressRepository;

  @Autowired
  NoteRepository noteRepository;

  @Autowired
  AddressTypeCodeRepository addressTypeCodeRepository;

  @Autowired
  ProvinceCodeRepository provinceCodeRepository;

  @Autowired
  CountryCodeRepository countryCodeRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @BeforeEach
  public void before() {
    this.districtRegionCodeRepository.save(this.createDistrictRegionCodeData());
    this.districtStatusCodeRepository.save(this.createDistrictStatusCodeData());
    this.districtContactTypeCodeRepository.save(this.createContactTypeCodeData());
    this.addressTypeCodeRepository.save(this.createAddressTypeCodeData());
    this.provinceCodeRepository.save(this.createProvinceCodeData());
    this.countryCodeRepository.save(this.createCountryCodeData());
  }

  /**
   * need to delete the records to make it working in unit tests assertion, else the records will keep growing and assertions will fail.
   */
  @AfterEach
  public void after() {
    this.addressRepository.deleteAll();
    this.districtContactRepository.deleteAll();
    this.noteRepository.deleteAll();
    this.districtRepository.deleteAll();
    this.districtHistoryRepository.deleteAll();
    this.districtRegionCodeRepository.deleteAll();
    this.districtStatusCodeRepository.deleteAll();
    this.districtContactTypeCodeRepository.deleteAll();
    this.addressTypeCodeRepository.deleteAll();
    this.provinceCodeRepository.deleteAll();
    this.countryCodeRepository.deleteAll();
  }

  @Test
  void testAllDistricts_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictEntity entity = this.districtRepository.save(this.createDistrictData());
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].districtId")
        .value(entity.getDistrictId().toString()));
  }

  @Test
  void testRetrieveDistrict_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictEntity entity = this.districtRepository.save(this.createDistrictData());
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/" + entity.getDistrictId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.districtId")
        .value(entity.getDistrictId().toString()));
  }

  @Test
  void testRetrieveDistrict_GivenInvalidID_ShouldReturnStatusNotFound() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/" + UUID.randomUUID()).with(mockAuthority))
      .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void testRetrieveDistrictHistory_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictEntity entity = this.districtRepository.save(this.createDistrictData());
    final DistrictHistoryEntity historyEntity = this.districtHistoryRepository.save(this.createHistoryDistrictData(entity.getDistrictId()));
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/" + entity.getDistrictId() + "/history").with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].districtHistoryId")
        .value(historyEntity.getDistrictHistoryId().toString()));
  }

  @Test
  void testRetrieveDistrictHistory_GivenInvalidID_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/abc/history").with(mockAuthority))
      .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void testRetrieveDistrict_GivenInvalidID_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/abc").with(mockAuthority))
      .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void testDeleteDistrict_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var district = this.createDistrictData();
    var entity = this.districtRepository.save(district);

    this.mockMvc.perform(delete(URL.BASE_URL_DISTRICT + "/" + entity.getDistrictId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_DISTRICT"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedEntity = this.districtRepository.findById(entity.getDistrictId());
    Assertions.assertTrue(deletedEntity.isEmpty());
  }

  @Test
  void testUpdateDistrict_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var district = this.createDistrictData();
    var entity = this.districtRepository.save(district);
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);

    this.mockMvc.perform(put(URL.BASE_URL_DISTRICT + "/" + entity.getDistrictId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(entity.getDisplayName()));
  }

  @Test
  void testUpdateDistrictWithAddress_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var district = this.createDistrictData();
    var entity = this.districtRepository.save(district);
    entity.getAddresses().add(this.createDistrictAddressData());
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);

    entity.getAddresses().stream().forEach(addy -> {
        addy.setCreateDate(null);
        addy.setUpdateDate(null);
      }
    );

    this.mockMvc.perform(put(URL.BASE_URL_DISTRICT + "/" + entity.getDistrictId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(entity.getDisplayName()));

    entity = this.districtRepository.findById(entity.getDistrictId()).get();
    var addr = this.createDistrictAddressData();
    addr.setAddressLine1("123 TESTING");
    entity.getAddresses().iterator().next().setAddressLine1("123 TESTING");
    entity.getAddresses().add(addr);

    DistrictMapper mapper = DistrictMapper.mapper;

    var auth = mapper.toStructure(entity);

    auth.setCreateDate(null);
    auth.setUpdateDate(null);

    auth.getAddresses().stream().forEach(addy -> {
        addy.setCreateDate(null);
        addy.setUpdateDate(null);
      }
    );

    this.mockMvc.perform(put(URL.BASE_URL_DISTRICT + "/" + entity.getDistrictId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(auth))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.addresses.[1].addressLine1").value("123 TESTING"));
  }

  @Test
  void testCreateDistrict_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var district = this.createDistrictData();
    district.setCreateDate(null);
    district.setUpdateDate(null);
    this.mockMvc.perform(post(URL.BASE_URL_DISTRICT)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(district))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(district.getDisplayName()));
  }

  @Test
  void testCreateDistrictWithAddress_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    var district = this.createDistrictData();
    var addressEntity = this.createDistrictAddressData();
    addressEntity.setCreateDate(null);
    addressEntity.setUpdateDate(null);
    district.getAddresses().add(addressEntity);
    district.setCreateDate(null);
    district.setUpdateDate(null);
    this.mockMvc.perform(post(URL.BASE_URL_DISTRICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(asJsonString(district))
                    .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT"))))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(district.getDisplayName()));
  }

  @Test
  void testCreateDistrict_GivenInvalidPayload_ShouldReturnStatusBadRequest() throws Exception {
    final var district = this.createDistrictData();
    district.setDistrictRegionCode("ABCD");
    district.setPhoneNumber("123");
    district.setFaxNumber("noletters");
    district.setCreateDate(null);
    district.setUpdateDate(null);
    this.mockMvc.perform(post(URL.BASE_URL_DISTRICT)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(district))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT"))))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void testCreateDistrictContact_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final DistrictEntity districtEntity = this.districtRepository.save(this.createDistrictData());
    DistrictContactEntity contactEntity = createContactData(districtEntity);
    contactEntity.setPhoneNumber("9876541234");
    contactEntity.setPhoneExtension("321");
    contactEntity.setAlternatePhoneNumber("1234567891");
    contactEntity.setAlternatePhoneExtension("123");
    contactEntity.setJobTitle("Painter");

    this.mockMvc.perform(post(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT_CONTACT"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(contactEntity.getLastName()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(contactEntity.getPhoneNumber()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.jobTitle").value(contactEntity.getJobTitle()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phoneExtension").value(contactEntity.getPhoneExtension()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.alternatePhoneNumber").value(contactEntity.getAlternatePhoneNumber()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.alternatePhoneExtension").value(contactEntity.getAlternatePhoneExtension()));
  }

  @Test
  void testCreateDistrictContact_GivenValidPayloadNoFirstName_ShouldReturnStatusCreated() throws Exception {
    final DistrictEntity districtEntity = this.districtRepository.save(this.createDistrictData());
    DistrictContactEntity contactEntity = createContactData(districtEntity);
    contactEntity.setPhoneNumber("9876541234");
    contactEntity.setPhoneExtension("321");
    contactEntity.setAlternatePhoneNumber("1234567891");
    contactEntity.setAlternatePhoneExtension("123");
    contactEntity.setJobTitle("Painter");
    contactEntity.setFirstName(null);

    this.mockMvc.perform(post(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT_CONTACT"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(contactEntity.getLastName()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(contactEntity.getPhoneNumber()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.jobTitle").value(contactEntity.getJobTitle()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phoneExtension").value(contactEntity.getPhoneExtension()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.alternatePhoneNumber").value(contactEntity.getAlternatePhoneNumber()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.alternatePhoneExtension").value(contactEntity.getAlternatePhoneExtension()));
  }

  @Test
  void testCreateDistrictContactExtFields_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final DistrictEntity districtEntity = this.districtRepository.save(this.createDistrictData());
    DistrictContactEntity contactEntity = createContactData(districtEntity);

    this.mockMvc.perform(post(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT_CONTACT"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(contactEntity.getLastName()));
  }

  @Test
  void testCreateDistrictContact_GivenInValidPayload_ShouldReturnStatusBadRequest() throws Exception {
    final DistrictEntity districtEntity = this.districtRepository.save(this.createDistrictData());
    DistrictContactEntity contactEntity = createContactData(districtEntity);
    contactEntity.setDistrictContactId(UUID.randomUUID());

    this.mockMvc.perform(post(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT_CONTACT"))))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void testCreateDistrictContact_GivenInValidTypeCodePayload_ShouldReturnStatusBadRequest() throws Exception {
    final DistrictEntity districtEntity = this.districtRepository.save(this.createDistrictData());
    DistrictContactEntity contactEntity = createContactData(districtEntity);
    contactEntity.setDistrictContactTypeCode("TESTER");

    this.mockMvc.perform(post(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT_CONTACT"))))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void testDeleteDistrictContact_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    DistrictContactEntity contactEntity = createContactData(districtEntity);
    var contact = this.districtContactRepository.save(contactEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/contact/" + contact.getDistrictContactId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(districtEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_DISTRICT_CONTACT"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedContact = this.districtContactRepository.findById(contact.getDistrictContactId());
    Assertions.assertTrue(deletedContact.isEmpty());
  }

  @Test
  void testRetrieveDistrictContact_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT_CONTACT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    DistrictContactEntity contactEntity = createContactData(districtEntity);
    var contact = this.districtContactRepository.save(contactEntity);
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/contact/" + contact.getDistrictContactId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.districtContactId")
        .value(contact.getDistrictContactId().toString()));
  }

  @ParameterizedTest
  @CsvSource(value = {"SCOPE_READ_DISTRICT_CONTACT:contact", "SCOPE_READ_DISTRICT_ADDRESS:address", "SCOPE_READ_DISTRICT_NOTE:note"}, delimiter = ':')
  void testRetrieveDistrictInstitute_GivenInvalidID_ShouldReturnStatusNotFound(String scope, String path) throws Exception {
    final GrantedAuthority grantedAuthority = () -> scope;
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/" + path + "/" + UUID.randomUUID()).with(mockAuthority))
      .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void testUpdateDistrictContact_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    DistrictContactEntity contactEntity = createContactData(districtEntity);
    var contact = this.districtContactRepository.save(contactEntity);
    contact.setFirstName("pete");

    this.mockMvc.perform(put(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/contact/" + contact.getDistrictContactId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contact))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT_CONTACT"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(contact.getFirstName()));
  }

  @Test
  void testCreateDistrictNote_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final DistrictEntity districtEntity = this.districtRepository.save(this.createDistrictData());
    NoteEntity noteEntity = createNoteData(districtEntity);

    this.mockMvc.perform(post(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/note")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(noteEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT_NOTE"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(noteEntity.getContent()));
  }

  @Test
  void testDeleteDistrictNote_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    NoteEntity noteEntity = createNoteData(districtEntity);
    var note = this.noteRepository.save(noteEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/note/" + note.getNoteId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(districtEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_DISTRICT_NOTE"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedNote = this.noteRepository.findById(note.getNoteId());
    Assertions.assertTrue(deletedNote.isEmpty());
  }

  @Test
  void testRetrieveDistrictNote_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT_NOTE";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    NoteEntity noteEntity = createNoteData(districtEntity);
    var note = this.noteRepository.save(noteEntity);
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/note/" + note.getNoteId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.noteId")
        .value(note.getNoteId().toString()));
  }

  @Test
  void testUpdateDistrictNote_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    NoteEntity noteEntity = createNoteData(districtEntity);
    var note = this.noteRepository.save(noteEntity);
    note.setContent("southshore");

    this.mockMvc.perform(put(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/note/" + note.getNoteId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(note))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT_NOTE"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(note.getContent()));
  }

  private DistrictEntity createDistrictData() {
    return DistrictEntity.builder().districtNumber("003").displayName("District Name").districtStatusCode("OPEN").districtRegionCode("KOOTENAYS")
      .website("abc@sd99.edu").createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private DistrictContactEntity createContactData(DistrictEntity entity) {
    return DistrictContactEntity.builder().districtEntity(entity).districtContactTypeCode("PRINCIPAL").firstName("John").lastName("Wayne").createUser("TEST").updateUser("TEST").build();
  }

  private NoteEntity createNoteData(DistrictEntity entity) {
    return NoteEntity.builder().districtID(entity.getDistrictId()).content("This is a note.").createUser("TEST").updateUser("TEST").build();
  }

  private DistrictHistoryEntity createHistoryDistrictData(UUID districtId) {
    return DistrictHistoryEntity.builder().districtId(districtId).districtNumber("003").displayName("District Name").districtRegionCode("KOOTENAYS").districtStatusCode("ACTIVE")
      .website("abc@sd99.edu").createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
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

  private DistrictRegionCodeEntity createDistrictRegionCodeData() {
    return DistrictRegionCodeEntity.builder().districtRegionCode("KOOTENAYS").description("Kootenays region")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Kootenays").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private DistrictStatusCodeEntity createDistrictStatusCodeData() {
    return DistrictStatusCodeEntity.builder().districtStatusCode("OPEN").description("Open Region.")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Open").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private DistrictContactTypeCodeEntity createContactTypeCodeData() {
    return DistrictContactTypeCodeEntity.builder().districtContactTypeCode("PRINCIPAL").description("School Principal")
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

  private DistrictAddressEntity createDistrictAddressData() {
    return DistrictAddressEntity.builder().addressLine1("Line 1").city("City").provinceCode("BC").countryCode("CA").postal("V1V1V2").addressTypeCode("MAILING")
      .createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();
  }

  private CountryCodeEntity createCountryCodeData() {
    return CountryCodeEntity.builder().countryCode("CA").description("Canada")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Canada").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

}


