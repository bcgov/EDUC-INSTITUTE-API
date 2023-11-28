package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.constants.v1.Constants;
import ca.bc.gov.educ.api.institute.constants.v1.FacilityCategoryLookup;
import ca.bc.gov.educ.api.institute.exception.InvalidParameterException;
import ca.bc.gov.educ.api.institute.exception.PreConditionFailedException;
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
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, "99%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return Constants.NINETYNINE + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.NINETYNINE)) {
                return getSchoolNumberWithinRange(districtNumber, null, getLowerBoundNumber(Constants.NINETYNINE), getUpperBoundNumber(Constants.NINETYNINE)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY2, (districtNumber, authorityId) -> {
            String pattern = districtNumber.length() > 2 ? districtNumber.substring(1) : districtNumber;
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, pattern + "%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return pattern + "011";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(pattern)) {
                Integer lowerBound  = Integer.parseInt(pattern + "010");
                return String.format("%05d", getSchoolNumberWithinRange(districtNumber, null, lowerBound, getUpperBoundNumber(pattern)));
            }
            return String.format("%05d", Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY3, (districtNumber, authorityId) -> {
            String schoolNumber = getFirstAvailableIndependentSchoolNumber();
            if(isNullEmptyOrBlank(schoolNumber)) {
                throw new PreConditionFailedException("Unable to generate school number. No more independent school numbers available. Please contact your System Administrator");
            }
            return schoolNumber;
        });

        map.put(FacilityCategoryLookup.ENTRY4, (districtNumber, authorityId) -> {
            String schoolNumber = getFirstAvailableIndependentFirstNationsSchoolNumber();
            if(isNullEmptyOrBlank(schoolNumber)) {
                throw new PreConditionFailedException("Unable to generate school number. No more independent First Nations school numbers available. Please contact your System Administrator");
            }
            return schoolNumber;
        });

        map.put(FacilityCategoryLookup.ENTRY5, (districtNumber, authorityId) -> {
            String pattern = districtNumber.length() > 2 ? districtNumber.substring(1) : districtNumber;
            Integer availableSchoolNumber = getFirstAvailableSchoolNumber(districtNumber, null, Integer.parseInt(pattern + "000"), Integer.parseInt(pattern + "010"));
            if(availableSchoolNumber == null || availableSchoolNumber == 0) {
                String nextSchoolNumber =  getLastSchoolNumberWithPattern(districtNumber, pattern + "%");
                return Integer.toString(Integer.parseInt(nextSchoolNumber) + 1);
            }
            return String.format("%05d", availableSchoolNumber);
        });

        map.put(FacilityCategoryLookup.ENTRY6, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, "25%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return Constants.TWENTYFIVE + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.TWENTYFIVE)) {
                return  getSchoolNumberWithinRange(districtNumber, null, getLowerBoundNumber(Constants.TWENTYFIVE), getUpperBoundNumber(Constants.TWENTYFIVE)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY7, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, "95%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return Constants.NINETYFIVE + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.NINETYFIVE)) {
                return getSchoolNumberWithinRange(districtNumber, null, getLowerBoundNumber(Constants.NINETYFIVE), getUpperBoundNumber(Constants.NINETYFIVE)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY8, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, "90%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return Constants.NINETY + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.NINETY)) {
                return getSchoolNumberWithinRange(districtNumber, null, getLowerBoundNumber(Constants.NINETY), getUpperBoundNumber(Constants.NINETY)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY9, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumberWithPattern(districtNumber, "94%");
            if(isNullEmptyOrBlank(schoolNumber)) {
                return Constants.NINETYFOUR + Constants.DEFAULTNUMBER;
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.NINETYFOUR)) {
                return getSchoolNumberWithinRange(districtNumber, null, getLowerBoundNumber(Constants.NINETYFOUR), getUpperBoundNumber(Constants.NINETYFOUR)).toString();
            }
            return Integer.toString(Integer.parseInt(schoolNumber) + 1);
        });

        map.put(FacilityCategoryLookup.ENTRY10, (districtNumber, authorityId) -> {
            String schoolNumber = getLastSchoolNumber("102", null);
            if(isNullEmptyOrBlank(schoolNumber)) {
                return "00001";
            } else if(Integer.parseInt(schoolNumber) == getUpperBoundNumber(Constants.NINETYNINE)) {
                return getSchoolNumberWithinRange("102", null, getLowerBoundNumber("00"), getUpperBoundNumber(Constants.NINETYNINE)).toString();
            }
            return String.format("%05d", Integer.parseInt(schoolNumber) + 1);
        });
    }

    private String getLastSchoolNumberWithPattern(String districtNumber, String pattern) {
        return schoolRepository.findLastSchoolNumberWithPattern(districtNumber, pattern);
    }

    private String getFirstAvailableIndependentSchoolNumber() {
        return schoolRepository.findFirstAvailableSchoolNumberForIndependent();
    }

    private String getFirstAvailableIndependentFirstNationsSchoolNumber() {
        return schoolRepository.findFirstAvailableSchoolNumberForIndependentFirstNations();
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
    private Integer getSchoolNumberWithinRange(String districtNumber, UUID authorityId, Integer lowerRange, Integer upperRange) {
        Integer schoolNumber = schoolRepository.findFirstAvailableSchoolNumber(districtNumber, authorityId, lowerRange, upperRange);
        if(schoolNumber == null || schoolNumber ==0) {
            throw new PreConditionFailedException("Unable to generate school number. Please contact your System Administrator");
        }
        return schoolNumber;
    }
}
