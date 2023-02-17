package ca.bc.gov.educ.api.institute.service;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.exception.InvalidParameterException;
import ca.bc.gov.educ.api.institute.model.v1.DistrictTombstoneEntity;
import ca.bc.gov.educ.api.institute.model.v1.FacilityTypeCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolCategoryCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
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
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    DistrictTombstoneRepository districtTombstoneRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @After
    public void tearDown() {
        schoolCategoryCodeRepository.deleteAll();
        facilityTypeCodeRepository.deleteAll();
        schoolRepository.deleteAll();
        districtTombstoneRepository.deleteAll();
    }
    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeDISTLEARN_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("DIST_LEARN"));
        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
        var schoolEntity = this.createSchoolData("PUBLIC", "DIST_LEARN");
        schoolEntity.setDistrictEntity(dist);
        this.schoolRepository.save(schoolEntity);
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("99000");
    }

    @Test
//    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeDISTLEARN_shouldGetAvailableSchoolNumber() {
//        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
//        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("DIST_LEARN"));
//        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
//        var schoolEntity = this.createSchoolDataWithSchoolNumber("99999","PUBLIC", "DIST_LEARN");
//        schoolEntity.setDistrictEntity(dist);
//        this.schoolRepository.save(schoolEntity);
//        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
//        assertThat(schoolNumber)
//                .isNotEmpty()
//                .startsWith("990");
//    }

    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeDISTLEARN_shouldGetNextSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("DIST_LEARN"));
        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
        var schoolEntity = this.createSchoolDataWithSchoolNumber("99997","PUBLIC", "DIST_LEARN");
        schoolEntity.setDistrictEntity(dist);
        this.schoolRepository.save(schoolEntity);
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("99998");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeYUKON_givenFacilityCodeDISTONLINE_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("YUKON"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("DISTONLINE"));
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("99000");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeINDEPEND_givenFacilityCodeALT_PROGS_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("INDEPEND"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("ALT_PROGS"));
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("99000");
    }

    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeSTANDARD_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("STANDARD"));
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("03011");
    }

    @Test
//    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeSTANDARD_shouldGetAvailableSchoolNumber() {
//        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
//        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("STANDARD"));
//        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
//        var schoolEntity = this.createSchoolDataWithSchoolNumber("03999","PUBLIC", "STANDARD");
//        schoolEntity.setDistrictEntity(dist);
//        this.schoolRepository.save(schoolEntity);
//        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
//        assertThat(schoolNumber)
//                .isNotEmpty();
//    }

    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeSTANDARD_shouldGetNextSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("STANDARD"));
        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
        var schoolEntity = this.createSchoolDataWithSchoolNumber("03997","PUBLIC", "STANDARD");
        schoolEntity.setDistrictEntity(dist);
        this.schoolRepository.save(schoolEntity);
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("03998");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeOFFSHORE_givenFacilityCodeSTANDARD_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("OFFSHORE"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("STANDARD"));
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), "d34972fe-49d7-9ef4-1e0f-e5a83ec77889");
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("96000");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeINDEPEND_givenFacilityCodeSTANDARD_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("INDEPEND"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("STANDARD"));
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), "d34972fe-49d7-9ef4-1e0f-e5a83ec77889");
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("96000");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeINDP_FNS_givenFacilityCodeSTANDARD_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("INDP_FNS"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("STANDARD"));
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("97000");
    }

    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeCONT_ED_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("CONT_ED"));
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("03000");
    }

    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeCONT_ED_shouldGetNextSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("CONT_ED"));
        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
        var schoolEntity = this.createSchoolDataWithSchoolNumber("03000","PUBLIC", "CONT_ED");
        schoolEntity.setDistrictEntity(dist);
        this.schoolRepository.save(schoolEntity);
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("03001");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeEAR_LEARN_givenFacilityCodeSTRONG_CEN_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("EAR_LEARN"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("STRONG_CEN"));
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("25000");
    }

//    @Test
//    public void testCreateSchool_givenSchoolCodeEAR_LEARN_givenFacilityCodeSTRONG_CEN_shouldGetAvailableSchoolNumber() {
//        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("EAR_LEARN"));
//        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("STRONG_CEN"));
//        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
//        var schoolEntity = this.createSchoolDataWithSchoolNumber("25999","EAR_LEARN", "STRONG_CEN");
//        schoolEntity.setDistrictEntity(dist);
//        this.schoolRepository.save(schoolEntity);
//        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
//        assertThat(schoolNumber)
//                .isNotEmpty();
//    }
    @Test
    public void testCreateSchool_givenSchoolCodeEAR_LEARN_givenFacilityCodeSTRONG_CEN_shouldGetNextSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("EAR_LEARN"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("STRONG_CEN"));
        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
        var schoolEntity = this.createSchoolDataWithSchoolNumber("25997","EAR_LEARN", "STRONG_CEN");
        schoolEntity.setDistrictEntity(dist);
        this.schoolRepository.save(schoolEntity);
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("25998");
    }

    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeSHORT_PRP_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("SHORT_PRP"));
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("95000");
    }

//    @Test
//    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeSHORT_PRP_shouldGetAvailableSchoolNumber() {
//        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
//        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("SHORT_PRP"));
//        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
//        var schoolEntity = this.createSchoolDataWithSchoolNumber("95999","PUBLIC", "SHORT_PRP");
//        schoolEntity.setDistrictEntity(dist);
//        this.schoolRepository.save(schoolEntity);
//        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
//        assertThat(schoolNumber)
//                .isNotEmpty();
//    }
    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeSHORT_PRP_shouldGetNextSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("SHORT_PRP"));
        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
        var schoolEntity = this.createSchoolDataWithSchoolNumber("95997","PUBLIC", "SHORT_PRP");
        schoolEntity.setDistrictEntity(dist);
        this.schoolRepository.save(schoolEntity);
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("95998");
    }

    @Test
    public void testCreateSchool_givenSchoolCodeYUKON_givenFacilityCodeSUMMER_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("YUKON"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("SUMMER"));
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("90000");
    }

//    @Test
//    public void testCreateSchool_givenSchoolCodeYUKON_givenFacilityCodeSUMMER_shouldGetAvailableSchoolNumber() {
//        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("YUKON"));
//        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("SUMMER"));
//        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
//        var schoolEntity = this.createSchoolDataWithSchoolNumber("90999","YUKON", "SUMMER");
//        schoolEntity.setDistrictEntity(dist);
//        this.schoolRepository.save(schoolEntity);
//        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
//        assertThat(schoolNumber)
//                .isNotEmpty();
//    }
    @Test
    public void testCreateSchool_givenSchoolCodeYUKON_givenFacilityCodeSUMMER_shouldGetNextSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("YUKON"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("SUMMER"));
        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
        var schoolEntity = this.createSchoolDataWithSchoolNumber("90997","YUKON", "SUMMER");
        schoolEntity.setDistrictEntity(dist);
        this.schoolRepository.save(schoolEntity);
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("90998");
    }

    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeYOUTH_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("YOUTH"));
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("94000");
    }

//    @Test
//    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeYOUTH_shouldGetAvailableSchoolNumber() {
//        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
//        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("YOUTH"));
//        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
//        var schoolEntity = this.createSchoolDataWithSchoolNumber("94999","PUBLIC", "YOUTH");
//        schoolEntity.setDistrictEntity(dist);
//        this.schoolRepository.save(schoolEntity);
//        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
//        assertThat(schoolNumber)
//                .isNotEmpty();
//    }
    @Test
    public void testCreateSchool_givenSchoolCodePUBLIC_givenFacilityCodeYOUTH_shouldGetNextSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("PUBLIC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("YOUTH"));
        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createDistrictData());
        var schoolEntity = this.createSchoolDataWithSchoolNumber("94997","PUBLIC", "YOUTH");
        schoolEntity.setDistrictEntity(dist);
        this.schoolRepository.save(schoolEntity);
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("94998");
    }

    @Test
    public void testCreateSchool_givenSchoolCodePOST_SEC_givenFacilityCodePOST_SEC_shouldCreateValidSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("POST_SEC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("POST_SEC"));
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("00001");
    }
//    @Test
//    public void testCreateSchool_givenSchoolCodePOST_SEC_givenFacilityCodePOST_shouldGetAvailableSchoolNumber() {
//        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("POST_SEC"));
//        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("POST_SEC"));
//        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createPosSecDistrictData());
//        var schoolEntity = this.createSchoolDataWithSchoolNumber("99999","POST_SEC", "POST_SEC");
//        schoolEntity.setDistrictEntity(dist);
//        this.schoolRepository.save(schoolEntity);
//        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
//        assertThat(schoolNumber)
//                .isNotEmpty();
//    }
    @Test
    public void testCreateSchool_givenSchoolCodePOST_SEC_givenFacilityCodePOST_shouldGetNextSchoolNumber() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("POST_SEC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("POST_SEC"));
        final DistrictTombstoneEntity dist = districtTombstoneRepository.save(createPosSecDistrictData());
        var schoolEntity = this.createSchoolDataWithSchoolNumber("00356","POST_SEC", "POST_SEC");
        schoolEntity.setDistrictEntity(dist);
        this.schoolRepository.save(schoolEntity);
        String schoolNumber = schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
        assertThat(schoolNumber)
                .isNotEmpty()
                .isEqualTo("00357");
    }

    @Test(expected = InvalidParameterException.class)
    public void testCreateSchool_givenSchoolCode_givenFacilityCode_shouldThrowError() {
        SchoolCategoryCodeEntity schoolCategoryCodeEntity = schoolCategoryCodeRepository.save(createSchoolCategoryCodeData("POST_SEC"));
        FacilityTypeCodeEntity facilityTypeCodeEntity = facilityTypeCodeRepository.save(createFacilityTypeCodeData("YOUTH"));
        this.schoolNumberGenerationService.generateSchoolNumber("003", facilityTypeCodeEntity.getFacilityTypeCode(), schoolCategoryCodeEntity.getSchoolCategoryCode(), null);
    }

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
    private DistrictTombstoneEntity createDistrictData() {
        return DistrictTombstoneEntity.builder().districtNumber("003").displayName("District Name").districtStatusCode("OPEN").districtRegionCode("KOOTENAYS")
                .website("abc@sd99.edu").createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
    }

    private DistrictTombstoneEntity createPosSecDistrictData() {
        return DistrictTombstoneEntity.builder().districtNumber("102").displayName("District Name").districtStatusCode("OPEN").districtRegionCode("KOOTENAYS")
                .website("abc@sd99.edu").createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
    }
    private SchoolEntity createSchoolData(String schoolCategory, String facilityTypeCode) {
        return SchoolEntity.builder().schoolNumber("12334").displayName("School Name").openedDate(LocalDateTime.now().minusDays(1).withNano(0)).schoolCategoryCode(schoolCategory)
                .schoolOrganizationCode("TWO_SEM").facilityTypeCode(facilityTypeCode).website("abc@sd99.edu").createDate(LocalDateTime.now().withNano(0))
                .updateDate(LocalDateTime.now().withNano(0)).createUser("TEST").updateUser("TEST").build();
    }

    private SchoolEntity createSchoolDataWithSchoolNumber(String schoolNumber, String schoolCategory, String facilityTypeCode) {
        return SchoolEntity.builder().schoolNumber(schoolNumber).displayName("School Name").openedDate(LocalDateTime.now().minusDays(1).withNano(0)).schoolCategoryCode(schoolCategory)
                .schoolOrganizationCode("TWO_SEM").facilityTypeCode(facilityTypeCode).website("abc@sd99.edu").createDate(LocalDateTime.now().withNano(0))
                .updateDate(LocalDateTime.now().withNano(0)).createUser("TEST").updateUser("TEST").build();
    }

}
