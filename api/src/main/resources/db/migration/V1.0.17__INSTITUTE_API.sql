INSERT INTO SCHOOL_CATEGORY_CODE (SCHOOL_CATEGORY_CODE, LABEL, DESCRIPTION, LEGACY_CODE,
                                  DISPLAY_ORDER, EFFECTIVE_DATE, EXPIRY_DATE, CREATE_USER,
                                  CREATE_DATE, UPDATE_USER, UPDATE_DATE)
VALUES ('INDP_FNS', 'Independent First Nations School', 'Independent First Nations School', null, 10,
        to_date('2020-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        to_date('2099-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));

UPDATE SCHOOL_CATEGORY_CODE
SET
    LABEL = 'Non-Independent First Nations School',
    DESCRIPTION = 'Non-Independent First Nations School'
WHERE
    SCHOOL_CATEGORY_CODE= 'FED_BAND';


