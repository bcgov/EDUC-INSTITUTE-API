package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.constants.v1.Constants;
import ca.bc.gov.educ.api.institute.constants.v1.FacilityCategoryLookup;
import ca.bc.gov.educ.api.institute.exception.InvalidParameterException;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolRepository;
import ca.bc.gov.educ.api.institute.util.Rules;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.UUID;

@Service
public class SchoolNumberGenerationService {

    private final SchoolRepository schoolRepository;

    private EnumMap<FacilityCategoryLookup, Rules> map;

    @Autowired
    public SchoolNumberGenerationService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
        init();
    }

    public String generateSchoolNumber(String districtNumber, String facilityCode, String categoryCode, String independentAuthorityId) {
        FacilityCategoryLookup value = FacilityCategoryLookup.getRuleToBeExecuted(facilityCode, categoryCode);
        if(value != null) {
            return map.get(value).generateNumber(districtNumber, independentAuthorityId);
        } else {
            throw new InvalidParameterException("School Number cannot be generated for this school category and facility type");
        }
    }

    @PostConstruct
    public void init() {
        map = new EnumMap<>(FacilityCategoryLookup.class);

        map.put(FacilityCategoryLookup.ENTRY1, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, "99%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return Constants.NINETYNINE + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.NINETYNINE)) {
                return getFirstAvailableSchoolNumber(districtNumber, null, getLowerBoundNumber(Constants.NINETYNINE), getUpperBoundNumber(Constants.NINETYNINE)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY2, (districtNumber, authorityId) -> {
            String pattern = districtNumber.length() > 2 ? districtNumber.substring(1) : districtNumber;
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, pattern + "%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return pattern + "011";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(pattern)) {
                Integer lowerBound  = Integer.parseInt(pattern + "010");
                return String.format("%05d", getFirstAvailableSchoolNumber(districtNumber, null, lowerBound, getUpperBoundNumber(pattern)));
            }
            return String.format("%05d", Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY3, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, UUID.fromString(authorityId), "96%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return Constants.NINETYSIX + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.NINETYSIX)) {
                return getFirstAvailableSchoolNumber(districtNumber, UUID.fromString(authorityId), getLowerBoundNumber(Constants.NINETYSIX), getUpperBoundNumber(Constants.NINETYSIX)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY4, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, Constants.NINETYSEVEN + "%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return Constants.NINETYSEVEN + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.NINETYSEVEN)) {
                return getFirstAvailableSchoolNumber(districtNumber, UUID.fromString(authorityId), getLowerBoundNumber(Constants.NINETYSEVEN), getUpperBoundNumber(Constants.NINETYSEVEN)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY5, (districtNumber, authorityId) -> {
            String pattern = districtNumber.length() > 2 ? districtNumber.substring(1) : districtNumber;
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, pattern + Constants.DEFAULTNUMBER);
            if(isNullEmptyOrBlank(schoolNumber)) {
                return pattern + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == Integer.parseInt(pattern + Constants.DEFAULTNUMBER)) {
                return String.format("%05d", Integer.parseInt(schoolNumber) + 1);
            }
            return schoolNumber;
        });

        map.put(FacilityCategoryLookup.ENTRY6, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, "25%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return Constants.TWENTYFIVE + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.TWENTYFIVE)) {
                return  getFirstAvailableSchoolNumber(districtNumber, null, getLowerBoundNumber(Constants.TWENTYFIVE), getUpperBoundNumber(Constants.TWENTYFIVE)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY7, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, "95%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return Constants.NINETYFIVE + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.NINETYFIVE)) {
                return getFirstAvailableSchoolNumber(districtNumber, null, getLowerBoundNumber(Constants.NINETYFIVE), getUpperBoundNumber(Constants.NINETYFIVE)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY8, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, "90%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return Constants.NINETY + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.NINETY)) {
                return getFirstAvailableSchoolNumber(districtNumber, null, getLowerBoundNumber(Constants.NINETY), getUpperBoundNumber(Constants.NINETY)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY9, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, null, "94%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return Constants.NINETYFOUR + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.NINETYFOUR)) {
                return getFirstAvailableSchoolNumber(districtNumber, null, getLowerBoundNumber(Constants.NINETYFOUR), getUpperBoundNumber(Constants.NINETYFOUR)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY10, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumber("102", null);
            if(isNullEmptyOrBlank(schoolNumber)) {
                return "00001";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.NINETYNINE)) {
                return getFirstAvailableSchoolNumber("102", null, getLowerBoundNumber("00"), getUpperBoundNumber(Constants.NINETYNINE)).toString();
            }
            return String.format("%05d", Integer.parseInt(schoolNumber) + 1);
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
        return Integer.parseInt(prefix + Constants.UPPERBOUND);
    }
    private Integer getLowerBoundNumber(String prefix) {
        return Integer.parseInt(prefix + Constants.LOWERBOUND);
    }
}
