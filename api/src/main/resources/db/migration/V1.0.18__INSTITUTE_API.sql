/* Updating display orders per https://gww.jira.educ.gov.bc.ca/browse/EDX-1910 */

UPDATE SCHOOL_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 60
WHERE
        SCHOOL_CONTACT_TYPE_CODE = 'STUDREGIS';