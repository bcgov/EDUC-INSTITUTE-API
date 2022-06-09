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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = InstituteApiResourceApplication.class)
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
  ContactTypeCodeRepository contactTypeCodeRepository;

  @Autowired
  ContactRepository contactRepository;

  @Autowired
  AddressRepository addressRepository;

  @Autowired
  AddressHistoryRepository addressHistoryRepository;

  @Autowired
  NoteRepository noteRepository;

  @Autowired
  AddressTypeCodeRepository addressTypeCodeRepository;

  @Autowired
  ProvinceCodeRepository provinceCodeRepository;

  @Autowired
  CountryCodeRepository countryCodeRepository;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Before
  public void before(){
    this.districtRegionCodeRepository.save(this.createDistrictRegionCodeData());
    this.contactTypeCodeRepository.save(this.createContactTypeCodeData());
    this.addressTypeCodeRepository.save(this.createAddressTypeCodeData());
    this.countryCodeRepository.save(this.createCountryCodeData());
    this.provinceCodeRepository.save(this.createProvinceCodeData());
  }

  /**
   * need to delete the records to make it working in unit tests assertion, else the records will keep growing and assertions will fail.
   */
  @After
  public void after() {
    this.addressRepository.deleteAll();
    this.contactRepository.deleteAll();
    this.noteRepository.deleteAll();
    this.districtRepository.deleteAll();
    this.districtHistoryRepository.deleteAll();
    this.districtRegionCodeRepository.deleteAll();
    this.contactTypeCodeRepository.deleteAll();
    this.addressTypeCodeRepository.deleteAll();
    this.provinceCodeRepository.deleteAll();
    this.countryCodeRepository.deleteAll();
    this.addressHistoryRepository.deleteAll();
  }

  @Test
  public void testAllDistricts_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictEntity entity = this.districtRepository.save(this.createDistrictData());
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].districtId")
        .value(entity.getDistrictId().toString()));
  }

  @Test
  public void testRetrieveDistrict_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictEntity entity = this.districtRepository.save(this.createDistrictData());
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/" + entity.getDistrictId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.districtId")
        .value(entity.getDistrictId().toString()));
  }

  @Test
  public void testRetrieveDistrictHistory_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictEntity entity = this.districtRepository.save(this.createDistrictData());
    final DistrictHistoryEntity historyEntity = this.districtHistoryRepository.save(this.createHistoryDistrictData(entity.getDistrictId()));
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/" + entity.getDistrictId() + "/history").with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].districtHistoryId")
        .value(historyEntity.getDistrictHistoryId().toString()));
  }

  @Test
  public void testRetrieveDistrictHistory_GivenInvalidID_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/abc/history").with(mockAuthority))
      .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void testRetrieveDistrict_GivenInvalidID_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/abc").with(mockAuthority))
      .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void testDeleteDistrict_GivenValidID_ShouldReturnStatusOK() throws Exception {
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
    Assert.assertTrue(deletedEntity.isEmpty());
  }

  @Test
  public void testUpdateDistrict_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
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
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(entity.getDisplayName().toUpperCase()));
  }

  @Test
  public void testCreateDistrict_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
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
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(district.getDisplayName().toUpperCase()));
  }

  @Test
  public void testCreateDistrictContact_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final DistrictEntity districtEntity = this.districtRepository.save(this.createDistrictData());
    ContactEntity contactEntity = createContactData(districtEntity);

    this.mockMvc.perform(post(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT_CONTACT"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(contactEntity.getLastName().toUpperCase()));
  }

  @Test
  public void testDeleteDistrictContact_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    ContactEntity contactEntity = createContactData(districtEntity);
    var contact = this.contactRepository.save(contactEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/contact/" + contact.getContactId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(districtEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_DISTRICT_CONTACT"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedContact = this.contactRepository.findById(contact.getContactId());
    Assert.assertTrue(deletedContact.isEmpty());
  }

  @Test
  public void testRetrieveDistrictContact_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT_CONTACT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    ContactEntity contactEntity = createContactData(districtEntity);
    var contact = this.contactRepository.save(contactEntity);
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/contact/" + contact.getContactId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.contactId")
        .value(contact.getContactId().toString()));
  }

  @Test
  public void testUpdateDistrictContact_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    ContactEntity contactEntity = createContactData(districtEntity);
    var contact = this.contactRepository.save(contactEntity);
    contact.setFirstName("pete");

    this.mockMvc.perform(put(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/contact/" + contact.getContactId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contact))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT_CONTACT"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(contact.getFirstName().toUpperCase()));
  }

  @Test
  public void testCreateDistrictAddress_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final DistrictEntity districtEntity = this.districtRepository.save(this.createDistrictData());
    AddressEntity addressEntity = createAddressData(districtEntity);

    this.mockMvc.perform(post(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/address")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(addressEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT_ADDRESS"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(addressEntity.getCity().toUpperCase()));

    var historyAddress = this.addressHistoryRepository.findAll();
    assertThat(historyAddress).isNotNull().hasSize(1);
  }

  @Test
  public void testDeleteDistrictAddress_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    AddressEntity addressEntity = createAddressData(districtEntity);
    var address = this.addressRepository.save(addressEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/address/" + address.getAddressId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(districtEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_DISTRICT_ADDRESS"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedAddress = this.addressRepository.findById(address.getAddressId());
    Assert.assertTrue(deletedAddress.isEmpty());
  }

  @Test
  public void testRetrieveDistrictAddress_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT_ADDRESS";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    AddressEntity addressEntity = createAddressData(districtEntity);
    var address = this.addressRepository.save(addressEntity);
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/address/" + address.getAddressId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.addressId")
        .value(address.getAddressId().toString()));
  }

  @Test
  public void testUpdateDistrictAddress_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var district = this.createDistrictData();
    var districtEntity = this.districtRepository.save(district);
    AddressEntity addressEntity = createAddressData(districtEntity);
    var address = this.addressRepository.save(addressEntity);
    address.setCity("southshore");

    this.mockMvc.perform(put(URL.BASE_URL_DISTRICT + "/" + districtEntity.getDistrictId() + "/address/" + address.getAddressId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(address))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT_ADDRESS"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(address.getCity().toUpperCase()));
  }

  @Test
  public void testCreateDistrictNote_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
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
  public void testDeleteDistrictNote_GivenValidID_ShouldReturnStatusOK() throws Exception {
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
    Assert.assertTrue(deletedNote.isEmpty());
  }

  @Test
  public void testRetrieveDistrictNote_GivenValidID_ShouldReturnStatusOK() throws Exception {
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
  public void testUpdateDistrictNote_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
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
    return DistrictEntity.builder().districtNumber("003").displayName("District Name").openedDate(LocalDateTime.now().minusDays(1)).districtRegionCode("KOOTENAYS")
      .website("abc@sd99.edu").createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private ContactEntity createContactData(DistrictEntity entity) {
    return ContactEntity.builder().districtEntity(entity).contactTypeCode("PRINCIPAL").firstName("John").lastName("Wayne").createUser("TEST").updateUser("TEST").build();
  }

  private AddressEntity createAddressData(DistrictEntity entity) {
    return AddressEntity.builder().districtEntity(entity).addressTypeCode("MAILING").addressLine1("123 This Street").city("Compton")
      .provinceCode("BC").countryCode("CAN").postal("V1B9H2").createUser("TEST").updateUser("TEST").build();
  }

  private NoteEntity createNoteData(DistrictEntity entity) {
    return NoteEntity.builder().districtEntity(entity).content("This is a note.").createUser("TEST").updateUser("TEST").build();
  }

  private DistrictHistoryEntity createHistoryDistrictData(UUID districtId) {
    return DistrictHistoryEntity.builder().districtId(districtId).districtNumber("003").displayName("District Name").openedDate(LocalDateTime.now().minusDays(1)).districtRegionCode("KOOTENAYS")
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
}


