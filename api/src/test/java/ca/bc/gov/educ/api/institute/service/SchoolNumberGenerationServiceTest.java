package ca.bc.gov.educ.api.institute.service;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.exception.InvalidParameterException;
import ca.bc.gov.educ.api.institute.model.v1.FacilityTypeCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolCategoryCodeEntity;
import ca.bc.gov.educ.api.institute.repository.v1.*;
import ca.bc.gov.educ.api.institute.service.v1.SchoolNumberGenerationService;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Slf4j
public class SchoolNumberGenerationServiceTest {

    @Autowired
    private SchoolNumberGenerationService schoolNumberGenerationService;
    @Autowired
    private FacilityTypeCodeRepository facilityTypeCodeRepository;

    @Autowired
    private SchoolCategoryCodeRepository schoolCategoryCodeRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @After
    public void tearDown() {
        schoolCategoryCodeRepository.deleteAll();
        facilityTypeCodeRepository.deleteAll();
    }
    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeDISTLEARN_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("DIST_LEARN"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("99011");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeYUKON_givenFacilityCodeDISTONLINE_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("YUKON"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("DISTONLINE"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("99011");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeINDEPEND_givenFacilityCodeALT_PROGS_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("INDEPEND"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("ALT_PROGS"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("99011");
    }

    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeSTANDARD_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("STANDARD"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("03011");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeOFFSHORE_givenFacilityCodeSTANDARD_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("OFFSHORE"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("STANDARD"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), "d34972fe-49d7-9ef4-1e0f-e5a83ec77889");
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("96011");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeINDEPEND_givenFacilityCodeSTANDARD_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("INDEPEND"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("STANDARD"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), "d34972fe-49d7-9ef4-1e0f-e5a83ec77889");
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("96011");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeINDP_FNS_givenFacilityCodeSTANDARD_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("INDP_FNS"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("STANDARD"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("97011");
    }

    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeCONT_ED_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("CONT_ED"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("03000");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeEAR_LEARN_givenFacilityCodeSTRONG_CEN_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("EAR_LEARN"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("STRONG_CEN"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("25011");
    }

    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeSHORT_PRP_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("SHORT_PRP"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("95011");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeYUKON_givenFacilityCodeSUMMER_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("YUKON"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("SUMMER"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("90011");
    }

    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeYOUTH_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("YOUTH"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("94011");
    }

    @Test
    public void testCreateSchool_givenSchoolCodePOST_SEC_givenFacilityCodePOST_SEC_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("POST_SEC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("POST_SEC"));
        String schoolNumber = this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("00011");
    }

//    @Test
//    public void testCreateSchool_givenSchoolCode_givenFacilityCode_shouldThrowError() {
//        SchoolCategoryCodeEntity schoolCategoryCodeEntity = this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData("POST_SEC"));
//        FacilityTypeCodeEntity facilityTypeCodeEntity = this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData("YOUTH"));
//        Assertions.assertThrows(InvalidParameterException.class, () -> {
//            this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
//        });
//
//    }

    private SchoolCategoryCodeEntity createSchoolCategoryCodeData(String code) {
        return SchoolCategoryCodeEntity.builder().schoolCategoryCode(code).description("Public School")
                .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Public School").createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
    }

    private FacilityTypeCodeEntity createFacilityTypeCodeData(String code) {
        return FacilityTypeCodeEntity.builder().facilityTypeCode(code).description("Standard School")
                .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Standard School").createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
    }
}
