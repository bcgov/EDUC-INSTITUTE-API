package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.constants.v1.FacilityCategoryLookup;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolRepository;
import ca.bc.gov.educ.api.institute.util.Rules;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.UUID;

@Service
public class SchoolNumberGenerationService {

    public static final String NINETY = "90";
    public static final String NINETYNINE = "99";
    public static final String NINETYSIX = "96";
    public static final String NINETYFIVE = "95";

    public static final String NINETYFOUR = "94";
    public static final String NINETYSEVEN = "97";
    public static final String LOWERBOUND = "010";
    public static final String TWENTYFIVE = "25";
    public static final String UPPERBOUND = "999";
    private final SchoolRepository schoolRepository;

    private EnumMap<FacilityCategoryLookup, Rules> map;

    @Autowired
    public SchoolNumberGenerationService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
        init();
    }

    public String generateSchoolNumber(String districtNumber, String facilityCode, String categoryCode, String independentAuthorityId) {
        FacilityCategoryLookup value = FacilityCategoryLookup.getRuleToBeExecuted(facilityCode, categoryCode);
        return map.get(value).generateNumber(districtNumber, independentAuthorityId);
    }

    @PostConstruct
    public void init() {
        map = new EnumMap<>(FacilityCategoryLookup.class);

        map.put(FacilityCategoryLookup.ENTRY1, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, "99%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return NINETYNINE + "011";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(NINETYNINE)) {
                return getFirstAvailableSchoolNumber(districtNumber, null, getLowerBoundNumber(NINETYNINE), getUpperBoundNumber(NINETYNINE)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY2, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumber(districtNumber, null);
            if(isNullEmptyOrBlank(schoolNumber)) {
                return districtNumber + "011";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(districtNumber)) {
                return getFirstAvailableSchoolNumber(districtNumber, null, getLowerBoundNumber(districtNumber), getUpperBoundNumber(districtNumber)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY3, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, UUID.fromString(authorityId), "96%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return NINETYSIX + "011";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(NINETYSIX)) {
                return getFirstAvailableSchoolNumber(districtNumber, UUID.fromString(authorityId), getLowerBoundNumber(NINETYSIX), getUpperBoundNumber(NINETYSIX)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY4, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, NINETYSEVEN + "%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return NINETYSEVEN + "011";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(NINETYSEVEN)) {
                return getFirstAvailableSchoolNumber(districtNumber, UUID.fromString(authorityId), getLowerBoundNumber(NINETYSEVEN), getUpperBoundNumber(NINETYSEVEN)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY5, (districtNumber, authorityId) -> {// check logic
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, districtNumber + "0000%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return districtNumber + "0000";
            } else if(Integer.parseInt(schoolNumber) == Integer.parseInt(districtNumber + "000")) {
                return Integer.toString(Integer.parseInt(schoolNumber) + 1);
            }
            return schoolNumber;
        });

        map.put(FacilityCategoryLookup.ENTRY6, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, "25%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return TWENTYFIVE + "011";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(TWENTYFIVE)) {
                return  getFirstAvailableSchoolNumber(districtNumber, null, getLowerBoundNumber(TWENTYFIVE), getUpperBoundNumber(TWENTYFIVE)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY7, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, "95%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return NINETYFIVE + "011";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(NINETYFIVE)) {
                return getFirstAvailableSchoolNumber(districtNumber, null, getLowerBoundNumber(NINETYFIVE), getUpperBoundNumber(NINETYFIVE)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY8, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, "90%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return NINETY + "011";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(NINETY)) {
                return getFirstAvailableSchoolNumber(districtNumber, null, getLowerBoundNumber(NINETY), getUpperBoundNumber(NINETY)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY9, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, "94%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return NINETYFOUR + "011";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(NINETYFOUR)) {
                return getFirstAvailableSchoolNumber(districtNumber, null, getLowerBoundNumber(NINETYFOUR), getUpperBoundNumber(NINETYFOUR)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY10, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumber(districtNumber, null);
            if(isNullEmptyOrBlank(schoolNumber)) {
                return "00011";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(NINETYNINE)) {
                return getFirstAvailableSchoolNumber(districtNumber, null, getLowerBoundNumber("00"), getUpperBoundNumber(NINETYNINE)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });
    }

    private String getLastSchoolNumberWithPattern(String districtNumber, UUID authorityId, String pattern) {
        return schoolRepository.findLastSchoolNumberWithPattern(districtNumber, authorityId, pattern);
    }
    private Integer getFirstAvailableSchoolNumber(String districtNumber, UUID authorityId, Integer lowerRange, Integer upperRange) {
        return schoolRepository.findFirstAvailableSchoolNumber(districtNumber, authorityId, lowerRange, upperRange);
    }
    private String getLastSchoolNumber(String districtNumber, UUID authorityId) {
        return schoolRepository.findLastSchoolNumber(districtNumber, authorityId);
    }
    private boolean isNullEmptyOrBlank(String s) {
        return s == null || s.isEmpty() || s.isBlank();
    }
    private Integer getUpperBoundNumber(String prefix) {
        return Integer.parseInt(prefix + UPPERBOUND);
    }
    private Integer getLowerBoundNumber(String prefix) {
        return Integer.parseInt(prefix + LOWERBOUND);
    }
}
