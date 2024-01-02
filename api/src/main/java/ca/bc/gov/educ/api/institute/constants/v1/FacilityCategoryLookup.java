package ca.bc.gov.educ.api.institute.constants.v1;

import java.util.Arrays;

public enum FacilityCategoryLookup {

    ENTRY1(new String[] {FacilityTypeCodes.DIST_LEARN.getCode(), FacilityTypeCodes.DISTONLINE.getCode(), FacilityTypeCodes.ALT_PROGS.getCode()},
            new String[]{SchoolCategoryCodes.PUBLIC.getCode(), SchoolCategoryCodes.YUKON.getCode()}),
    ENTRY2(new String[] {FacilityTypeCodes.STANDARD.getCode()}, new String[]{SchoolCategoryCodes.PUBLIC.getCode(), SchoolCategoryCodes.YUKON.getCode()}),
    ENTRY3(new String[] {FacilityTypeCodes.DIST_LEARN.getCode(), FacilityTypeCodes.STANDARD.getCode()}, new String[]{SchoolCategoryCodes.OFFSHORE.getCode(), SchoolCategoryCodes.INDEPEND.getCode()}),
    ENTRY4(new String[] {FacilityTypeCodes.STANDARD.getCode()}, new String[]{SchoolCategoryCodes.INDP_FNS.getCode(), SchoolCategoryCodes.FED_BAND.getCode()}),
    ENTRY5(new String[] {FacilityTypeCodes.CONT_ED.getCode()}, new String[]{SchoolCategoryCodes.PUBLIC.getCode()}),
    ENTRY6(new String[] {FacilityTypeCodes.STRONG_CEN.getCode(), FacilityTypeCodes.STRONG_OUT.getCode(), FacilityTypeCodes.JUSTB4PRO.getCode()},
            new String[]{SchoolCategoryCodes.EAR_LEARN.getCode()}),
    ENTRY7(new String[] {FacilityTypeCodes.SHORT_PRP.getCode(), FacilityTypeCodes.LONG_PRP.getCode() }, new String[]{SchoolCategoryCodes.PUBLIC.getCode()}),
    ENTRY8(new String[] {FacilityTypeCodes.SUMMER.getCode()}, new String[]{SchoolCategoryCodes.PUBLIC.getCode(), SchoolCategoryCodes.YUKON.getCode()}),
    ENTRY9(new String[] {FacilityTypeCodes.YOUTH.getCode()}, new String[]{SchoolCategoryCodes.PUBLIC.getCode()}),
    ENTRY10(new String[] {FacilityTypeCodes.POST_SEC.getCode()}, new String[]{SchoolCategoryCodes.POST_SEC.getCode()});

    private final String[] facilityCodes;
    private final String[] mappedCategories;
    FacilityCategoryLookup(String[] facilityCodes, String[] mappedCategories) {
        this.facilityCodes = facilityCodes;
        this.mappedCategories= mappedCategories;
    }
    public static FacilityCategoryLookup getRuleToBeExecuted(String facilityTypeCode, String categoryCode) {
        return Arrays.stream(values())
                .filter(e -> Arrays.asList(e.facilityCodes).contains(facilityTypeCode) && Arrays.asList(e.mappedCategories).contains(categoryCode))
                .findFirst().orElse(null);
    }

}
