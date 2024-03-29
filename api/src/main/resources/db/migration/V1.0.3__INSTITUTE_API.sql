/* Updating display orders as per https://gww.jira.educ.gov.bc.ca/browse/EDX-665 */

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 1
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'SUPER';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 2
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'CHAIR';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 3
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'SECRETARY';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 4
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'ADMN_ASSIS';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 5
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'INDIGENOUS';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 6
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'CUSTORDER';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 7
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'EARL_LEARN';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 8
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'FACILITIES';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 9
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'FINANCIAL';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 10
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'FRENCH';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 11
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'INTERNAT';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 12
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'LITERACY';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 13
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'MYED';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 14
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'INCLUSIVE';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 15
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'TRANSPORT';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 16
WHERE
    DISTRICT_CONTACT_TYPE_CODE = '1701';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 17
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'DIST_LEARN';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 18
WHERE
    DISTRICT_CONTACT_TYPE_CODE = 'PLANNING';

UPDATE DISTRICT_CONTACT_TYPE_CODE
SET
    DISPLAY_ORDER = 19
WHERE
        DISTRICT_CONTACT_TYPE_CODE = 'EAR_CHILD';