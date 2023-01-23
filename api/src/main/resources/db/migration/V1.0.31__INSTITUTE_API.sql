INSERT INTO SCHOOL_CATEGORY_CODE (SCHOOL_CATEGORY_CODE, LABEL, DESCRIPTION, LEGACY_CODE,
                                  DISPLAY_ORDER, EFFECTIVE_DATE, EXPIRY_DATE, CREATE_USER,
                                  CREATE_DATE, UPDATE_USER, UPDATE_DATE)
VALUES ('SPEC_RPRT', 'Special Reporting', 'Special Reporting School', NULL, 10,
        to_date('2020-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        to_date('2099-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/TRERICHA',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/TRERICHA',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));

UPDATE school
SET school_category_code = 'SPEC_RPRT'
WHERE district_id = (SELECT district_id FROM district WHERE district_number = '091')
  AND school_number = '96531';

UPDATE school
SET school_category_code = 'SPEC_RPRT'
WHERE district_id = (SELECT district_id FROM district WHERE district_number = '091')
  AND school_number = '96532';

UPDATE school
SET school_category_code = 'SPEC_RPRT'
WHERE district_id = (SELECT district_id FROM district WHERE district_number = '060')
  AND school_number = '96658';

UPDATE school
SET school_category_code = 'SPEC_RPRT'
WHERE district_id = (SELECT district_id FROM district WHERE district_number = '060')
  AND school_number =  '96677';

UPDATE school
SET school_category_code = 'SPEC_RPRT'
WHERE district_id = (SELECT district_id FROM district WHERE district_number = '091')
  AND school_number = '96415';
