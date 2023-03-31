ALTER TABLE SCHOOL_HISTORY
  ADD COLUMN SCHOOL_REPORTING_REQUIREMENT_CODE VARCHAR(10) DEFAULT 'REGULAR' NOT NULL;

ALTER TABLE SCHOOL_HISTORY
  ADD CONSTRAINT SCHOOL_REPORTING_REQUIREMENT_CODE_FK
    FOREIGN KEY (SCHOOL_REPORTING_REQUIREMENT_CODE)
      REFERENCES SCHOOL_REPORTING_REQUIREMENT_CODE(SCHOOL_REPORTING_REQUIREMENT_CODE);

UPDATE SCHOOL_HISTORY
  SET SCHOOL_REPORTING_REQUIREMENT_CODE = 'NONE'
  WHERE SCHOOL_CATEGORY_CODE IN ('YUKON', 'POST_SEC', 'EAR_LEARN')
  OR SCHOOL_ORGANIZATION_CODE = 'SPEC_RPRT';

UPDATE SCHOOL_HISTORY
  SET SCHOOL_REPORTING_REQUIREMENT_CODE = 'CSF'
  WHERE DISTRICT_ID = (SELECT DISTRICT_ID FROM DISTRICT WHERE DISTRICT_NUMBER = '093');

UPDATE SCHOOL_HISTORY
  SET SCHOOL_REPORTING_REQUIREMENT_CODE = 'RT'
  FROM DISTRICT,
  (VALUES
    ('005', '97007'),
    ('022', '97070'),
    ('027', '97017'),
    ('027', '97038'),
    ('027', '97084'),
    ('027', '97171'),
    ('033', '97044'),
    ('033', '97082'),
    ('044', '97058'),
    ('045', '97081'),
    ('048', '97085'),
    ('050', '97001'),
    ('053', '97069'),
    ('058', '97075'),
    ('059', '97059'),
    ('063', '97060'),
    ('063', '97073'),
    ('067', '97079'),
    ('068', '97042'),
    ('068', '97052'),
    ('068', '97078'),
    ('070', '97037'),
    ('073', '97011'),
    ('073', '97492'),
    ('073', '97035'),
    ('074', '97031'),
    ('074', '97062'),
    ('078', '97034'),
    ('078', '97494'),
    ('079', '97046'),
    ('079', '97055'),
    ('082', '97024'),
    ('082', '97043'),
    ('082', '97083'),
    ('083', '97039'),
    ('085', '97049'),
    ('085', '97057'),
    ('091', '97040'),
    ('091', '97056'),
    ('091', '97071'),
    ('091', '97486')
  ) AS MINCODE (DISTRICT_NUMBER, SCHOOL_NUMBER)
  WHERE DISTRICT.DISTRICT_ID = (
        SELECT DISTRICT_ID
        FROM DISTRICT
        WHERE DISTRICT_NUMBER = MINCODE.DISTRICT_NUMBER
    )
    AND SCHOOL_HISTORY.SCHOOL_NUMBER = MINCODE.SCHOOL_NUMBER;
