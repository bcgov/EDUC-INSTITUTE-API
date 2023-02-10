package ca.bc.gov.educ.api.institute.constants.v1;

import java.util.Arrays;

public enum FacilityCategoryLookup {
    ENTRY1(new String[] {"DIST_LEARN", "DISTONLINE", "ALT_PROGS"}, new String[]{"PUBLIC", "YUKON", "INDEPEND"}),
    ENTRY2(new String[] {"STANDARD"}, new String[]{"PUBLIC", "YUKON"}),
    ENTRY3(new String[] {"STANDARD"}, new String[]{"OFFSHORE", "INDEPEND"}),
    ENTRY4(new String[] {"STANDARD"}, new String[]{"INDP_FNS", "FED_BAND"}),
    ENTRY5(new String[] {"CONT_ED"}, new String[]{"PUBLIC"}),
    ENTRY6(new String[] {"STRONG_CEN", "STRONG_OUT", "STRONG_OUT"}, new String[]{"EAR_LEARN"}),
    ENTRY7(new String[] {"SHORT_PRP", "LONG_PRP" }, new String[]{"PUBLIC"}),
    ENTRY8(new String[] {"SUMMER"}, new String[]{"PUBLIC", "YUKON"}),
    ENTRY9(new String[] {"YOUTH"}, new String[]{"PUBLIC"}),
    ENTRY10(new String[] {"POST_SEC"}, new String[]{"POST_SEC"})
    ;

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
