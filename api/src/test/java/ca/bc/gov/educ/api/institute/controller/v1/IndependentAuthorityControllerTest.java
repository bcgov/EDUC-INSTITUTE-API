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
public class IndependentAuthorityControllerTest {

  private static final CodeTableMapper mapper = CodeTableMapper.mapper;
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  CodeTableAPIController controller;

  @Autowired
  IndependentAuthorityRepository independentAuthorityRepository;

  @Autowired
  IndependentAuthorityHistoryRepository independentAuthorityHistoryRepository;

  @Autowired
  CodeTableService codeTableService;

  @Autowired
  AuthorityTypeCodeRepository authorityTypeCodeRepository;

  @Autowired
  AuthorityGroupCodeRepository authorityGroupCodeRepository;

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


  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Before
  public void before(){
    this.authorityTypeCodeRepository.save(this.createAuthorityTypeCodeData());
    this.authorityGroupCodeRepository.save(this.createAuthorityGroupCodeData());
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
    this.independentAuthorityRepository.deleteAll();
    this.independentAuthorityHistoryRepository.deleteAll();
    this.authorityTypeCodeRepository.deleteAll();
    this.authorityGroupCodeRepository.deleteAll();
  }

  @Test
  public void testAllIndependentAuthoritys_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INDEPENDENT_AUTHORITY";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final IndependentAuthorityEntity entity = this.independentAuthorityRepository.save(this.createIndependentAuthorityData());
    this.mockMvc.perform(get(URL.BASE_URL_AUTHORITY).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].independentAuthorityId")
        .value(entity.getIndependentAuthorityId().toString()));
  }

  @Test
  public void testRetrieveIndependentAuthority_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INDEPENDENT_AUTHORITY";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final IndependentAuthorityEntity entity = this.independentAuthorityRepository.save(this.createIndependentAuthorityData());
    this.mockMvc.perform(get(URL.BASE_URL_AUTHORITY + "/" + entity.getIndependentAuthorityId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.independentAuthorityId")
        .value(entity.getIndependentAuthorityId().toString()));
  }

  @Test
  public void testRetrieveIndependentAuthorityHistory_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INDEPENDENT_AUTHORITY";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final IndependentAuthorityEntity entity = this.independentAuthorityRepository.save(this.createIndependentAuthorityData());
    final IndependentAuthorityHistoryEntity historyEntity = this.independentAuthorityHistoryRepository.save(this.createHistoryIndependentAuthorityData(entity.getIndependentAuthorityId()));
    this.mockMvc.perform(get(URL.BASE_URL_AUTHORITY + "/" + entity.getIndependentAuthorityId() + "/history").with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].independentAuthorityHistoryId")
        .value(historyEntity.getIndependentAuthorityHistoryId().toString()));
  }

  @Test
  public void testRetrieveIndependentAuthorityHistory_GivenInvalidID_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INDEPENDENT_AUTHORITY";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_AUTHORITY + "/abc/history").with(mockAuthority))
      .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void testRetrieveIndependentAuthority_GivenInvalidID_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INDEPENDENT_AUTHORITY";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_AUTHORITY + "/abc").with(mockAuthority))
      .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void testDeleteIndependentAuthority_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var independentAuthority = this.createIndependentAuthorityData();
    var entity = this.independentAuthorityRepository.save(independentAuthority);

    this.mockMvc.perform(delete(URL.BASE_URL_AUTHORITY + "/" + entity.getIndependentAuthorityId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_INDEPENDENT_AUTHORITY"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedEntity = this.independentAuthorityRepository.findById(entity.getIndependentAuthorityId());
    Assert.assertTrue(deletedEntity.isEmpty());
  }

  @Test
  public void testUpdateIndependentAuthority_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var independentAuthority = this.createIndependentAuthorityData();
    var entity = this.independentAuthorityRepository.save(independentAuthority);
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);

    this.mockMvc.perform(put(URL.BASE_URL_AUTHORITY + "/" + entity.getIndependentAuthorityId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_INDEPENDENT_AUTHORITY"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(entity.getDisplayName().toUpperCase()));
  }

  @Test
  public void testCreateIndependentAuthority_GivenValidPayload_ShouldReturnStatusOK() throws Exception {
    final var independentAuthority = this.createIndependentAuthorityData();
    independentAuthority.setCreateDate(null);
    independentAuthority.setUpdateDate(null);
    this.mockMvc.perform(post(URL.BASE_URL_AUTHORITY)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(independentAuthority))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_INDEPENDENT_AUTHORITY"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(independentAuthority.getDisplayName().toUpperCase()));
  }

  @Test
  public void testCreateIndependentAuthorityContact_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final IndependentAuthorityEntity independentAuthorityEntity = this.independentAuthorityRepository.save(this.createIndependentAuthorityData());
    ContactEntity contactEntity = createContactData(independentAuthorityEntity);

    this.mockMvc.perform(post(URL.BASE_URL_AUTHORITY + "/" + independentAuthorityEntity.getIndependentAuthorityId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_INDEPENDENT_AUTHORITY_CONTACT"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(contactEntity.getLastName().toUpperCase()));
  }

  @Test
  public void testDeleteIndependentAuthorityContact_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var independentAuthority = this.createIndependentAuthorityData();
    var independentAuthorityEntity = this.independentAuthorityRepository.save(independentAuthority);
    ContactEntity contactEntity = createContactData(independentAuthorityEntity);
    var contact = this.contactRepository.save(contactEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_AUTHORITY + "/" + independentAuthorityEntity.getIndependentAuthorityId() + "/contact/" + contact.getContactId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(independentAuthorityEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_INDEPENDENT_AUTHORITY_CONTACT"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedContact = this.contactRepository.findById(contact.getContactId());
    Assert.assertTrue(deletedContact.isEmpty());
  }

  @Test
  public void testRetrieveIndependentAuthorityContact_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INDEPENDENT_AUTHORITY_CONTACT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var independentAuthority = this.createIndependentAuthorityData();
    var independentAuthorityEntity = this.independentAuthorityRepository.save(independentAuthority);
    ContactEntity contactEntity = createContactData(independentAuthorityEntity);
    var contact = this.contactRepository.save(contactEntity);
    this.mockMvc.perform(get(URL.BASE_URL_AUTHORITY + "/" + independentAuthorityEntity.getIndependentAuthorityId() + "/contact/" + contact.getContactId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.contactId")
        .value(contact.getContactId().toString()));
  }

  @Test
  public void testUpdateIndependentAuthorityContact_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var independentAuthority = this.createIndependentAuthorityData();
    var independentAuthorityEntity = this.independentAuthorityRepository.save(independentAuthority);
    ContactEntity contactEntity = createContactData(independentAuthorityEntity);
    var contact = this.contactRepository.save(contactEntity);
    contact.setFirstName("pete");

    this.mockMvc.perform(put(URL.BASE_URL_AUTHORITY + "/" + independentAuthorityEntity.getIndependentAuthorityId() + "/contact/" + contact.getContactId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contact))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_INDEPENDENT_AUTHORITY_CONTACT"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(contact.getFirstName().toUpperCase()));
  }

  @Test
  public void testCreateIndependentAuthorityAddress_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final IndependentAuthorityEntity independentAuthorityEntity = this.independentAuthorityRepository.save(this.createIndependentAuthorityData());
    AddressEntity addressEntity = createAddressData(independentAuthorityEntity);

    this.mockMvc.perform(post(URL.BASE_URL_AUTHORITY + "/" + independentAuthorityEntity.getIndependentAuthorityId() + "/address")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(addressEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_INDEPENDENT_AUTHORITY_ADDRESS"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(addressEntity.getCity().toUpperCase()));
  }

  @Test
  public void testDeleteIndependentAuthorityAddress_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var independentAuthority = this.createIndependentAuthorityData();
    var independentAuthorityEntity = this.independentAuthorityRepository.save(independentAuthority);
    AddressEntity addressEntity = createAddressData(independentAuthorityEntity);
    var address = this.addressRepository.save(addressEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_AUTHORITY + "/" + independentAuthorityEntity.getIndependentAuthorityId() + "/address/" + address.getAddressId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(independentAuthorityEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_INDEPENDENT_AUTHORITY_ADDRESS"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedAddress = this.addressRepository.findById(address.getAddressId());
    Assert.assertTrue(deletedAddress.isEmpty());
  }

  @Test
  public void testRetrieveIndependentAuthorityAddress_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INDEPENDENT_AUTHORITY_ADDRESS";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var independentAuthority = this.createIndependentAuthorityData();
    var independentAuthorityEntity = this.independentAuthorityRepository.save(independentAuthority);
    AddressEntity addressEntity = createAddressData(independentAuthorityEntity);
    var address = this.addressRepository.save(addressEntity);
    this.mockMvc.perform(get(URL.BASE_URL_AUTHORITY + "/" + independentAuthorityEntity.getIndependentAuthorityId() + "/address/" + address.getAddressId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.addressId")
        .value(address.getAddressId().toString()));
  }

  @Test
  public void testUpdateIndependentAuthorityAddress_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var independentAuthority = this.createIndependentAuthorityData();
    var independentAuthorityEntity = this.independentAuthorityRepository.save(independentAuthority);
    AddressEntity addressEntity = createAddressData(independentAuthorityEntity);
    var address = this.addressRepository.save(addressEntity);
    address.setCity("southshore");

    this.mockMvc.perform(put(URL.BASE_URL_AUTHORITY + "/" + independentAuthorityEntity.getIndependentAuthorityId() + "/address/" + address.getAddressId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(address))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_INDEPENDENT_AUTHORITY_ADDRESS"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(address.getCity().toUpperCase()));
  }

  @Test
  public void testCreateIndependentAuthorityNote_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final IndependentAuthorityEntity independentAuthorityEntity = this.independentAuthorityRepository.save(this.createIndependentAuthorityData());
    NoteEntity noteEntity = createNoteData(independentAuthorityEntity);

    this.mockMvc.perform(post(URL.BASE_URL_AUTHORITY + "/" + independentAuthorityEntity.getIndependentAuthorityId() + "/note")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(noteEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_INDEPENDENT_AUTHORITY_NOTE"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(noteEntity.getContent()));
  }

  @Test
  public void testDeleteIndependentAuthorityNote_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var independentAuthority = this.createIndependentAuthorityData();
    var independentAuthorityEntity = this.independentAuthorityRepository.save(independentAuthority);
    NoteEntity noteEntity = createNoteData(independentAuthorityEntity);
    var note = this.noteRepository.save(noteEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_AUTHORITY + "/" + independentAuthorityEntity.getIndependentAuthorityId() + "/note/" + note.getNoteId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(independentAuthorityEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_INDEPENDENT_AUTHORITY_NOTE"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedNote = this.noteRepository.findById(note.getNoteId());
    Assert.assertTrue(deletedNote.isEmpty());
  }

  @Test
  public void testRetrieveIndependentAuthorityNote_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INDEPENDENT_AUTHORITY_NOTE";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var independentAuthority = this.createIndependentAuthorityData();
    var independentAuthorityEntity = this.independentAuthorityRepository.save(independentAuthority);
    NoteEntity noteEntity = createNoteData(independentAuthorityEntity);
    var note = this.noteRepository.save(noteEntity);
    this.mockMvc.perform(get(URL.BASE_URL_AUTHORITY + "/" + independentAuthorityEntity.getIndependentAuthorityId() + "/note/" + note.getNoteId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.noteId")
        .value(note.getNoteId().toString()));
  }

  @Test
  public void testUpdateIndependentAuthorityNote_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var independentAuthority = this.createIndependentAuthorityData();
    var independentAuthorityEntity = this.independentAuthorityRepository.save(independentAuthority);
    NoteEntity noteEntity = createNoteData(independentAuthorityEntity);
    var note = this.noteRepository.save(noteEntity);
    note.setContent("southshore");

    this.mockMvc.perform(put(URL.BASE_URL_AUTHORITY + "/" + independentAuthorityEntity.getIndependentAuthorityId() + "/note/" + note.getNoteId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(note))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_INDEPENDENT_AUTHORITY_NOTE"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(note.getContent()));
  }

  private IndependentAuthorityEntity createIndependentAuthorityData() {
    return IndependentAuthorityEntity.builder().authorityNumber("003").displayName("IndependentAuthority Name").openedDate(LocalDateTime.now().minusDays(1))
      .authorityTypeCode("INDEPEND").authorityGroupCode("ACC_CHRIS").createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private IndependentAuthorityHistoryEntity createHistoryIndependentAuthorityData(UUID independentAuthorityId) {
    return IndependentAuthorityHistoryEntity.builder().independentAuthorityId(independentAuthorityId).authorityNumber("003").displayName("IndependentAuthority Name")
      .openedDate(LocalDateTime.now().minusDays(1)).authorityTypeCode("INDEPEND").authorityGroupCode("ACC_CHRIS")
      .createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
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

  private AuthorityGroupCodeEntity createAuthorityGroupCodeData() {
    return AuthorityGroupCodeEntity.builder().authorityGroupCode("ACC_CHRIS").description("Accelerated Christian Educ Association")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Accelerated Christian Ed.").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private AuthorityTypeCodeEntity createAuthorityTypeCodeData() {
    return AuthorityTypeCodeEntity.builder().authorityTypeCode("INDEPEND").description("Independent School")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Independent School").createDate(LocalDateTime.now())
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


  private ContactEntity createContactData(IndependentAuthorityEntity entity) {
    return ContactEntity.builder().independentAuthorityEntity(entity).contactTypeCode("PRINCIPAL").firstName("John").lastName("Wayne").createUser("TEST").updateUser("TEST").build();
  }

  private AddressEntity createAddressData(IndependentAuthorityEntity entity) {
    return AddressEntity.builder().independentAuthorityEntity(entity).addressTypeCode("MAILING").addressLine1("123 This Street").city("Compton")
      .provinceCode("BC").countryCode("CAN").postal("V1B9H2").createUser("TEST").updateUser("TEST").build();
  }

  private NoteEntity createNoteData(IndependentAuthorityEntity entity) {
    return NoteEntity.builder().independentAuthorityEntity(entity).content("This is a note.").createUser("TEST").updateUser("TEST").build();
  }
}


