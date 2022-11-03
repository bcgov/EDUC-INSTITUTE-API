INSERT INTO FACILITY_TYPE_CODE (FACILITY_TYPE_CODE, LABEL, DESCRIPTION, LEGACY_CODE,
                                DISPLAY_ORDER, EFFECTIVE_DATE, EXPIRY_DATE, CREATE_USER,
                                CREATE_DATE, UPDATE_USER, UPDATE_DATE)
VALUES ('DISTONLINE', 'District Online Learning', 'District online learning school', null, 11,
        to_date('2020-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        to_date('2099-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO FACILITY_TYPE_CODE (FACILITY_TYPE_CODE, LABEL, DESCRIPTION, LEGACY_CODE,
                                DISPLAY_ORDER, EFFECTIVE_DATE, EXPIRY_DATE, CREATE_USER,
                                CREATE_DATE, UPDATE_USER, UPDATE_DATE)
VALUES ('JUSTB4PRO', 'JustB4 Program', 'JustB4 program', null, 12,
        to_date('2020-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        to_date('2099-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));

UPDATE FACILITY_TYPE_CODE
SET
    LABEL = 'Provincial Online Learning',
    DESCRIPTION = 'Provincial online learning school'
WHERE
        FACILITY_TYPE_CODE = 'DIST_LEARN';
