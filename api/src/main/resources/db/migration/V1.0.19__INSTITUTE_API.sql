INSERT INTO SCHOOL_CONTACT_TYPE_CODE (SCHOOL_CONTACT_TYPE_CODE, LABEL, DESCRIPTION, LEGACY_CODE,
                                      DISPLAY_ORDER, EFFECTIVE_DATE, EXPIRY_DATE, CREATE_USER,
                                      CREATE_DATE, UPDATE_USER, UPDATE_DATE, PUBLICLY_AVAIL, IND_ONLY, OFFSHORE_ONLY)
VALUES ('SFECOORDBU', 'Safe Schools Coord. (Backup)', 'Safe Schools Coordinator (Backup)', null, 41,
        to_date('2020-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        to_date('2099-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), false, true, false);
