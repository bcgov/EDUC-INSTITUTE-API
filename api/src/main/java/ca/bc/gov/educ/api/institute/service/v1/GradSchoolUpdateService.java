package ca.bc.gov.educ.api.institute.service.v1;


import ca.bc.gov.educ.api.institute.constants.v1.EventType;
import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.repository.v1.InstituteEventRepository;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolRepository;
import ca.bc.gov.educ.api.institute.struct.v1.external.gradschool.GradSchool;
import ca.bc.gov.educ.api.institute.util.TransformUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


/**
 * The type School update service.
 */
@Service
@Slf4j
public class GradSchoolUpdateService extends BaseService<GradSchool> {

    private final InstituteEventRepository instituteEventRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolHistoryService schoolHistoryService;

    public GradSchoolUpdateService(InstituteEventRepository instituteEventRepository, SchoolRepository schoolRepository, SchoolHistoryService schoolHistoryService) {
        super(instituteEventRepository);
        this.instituteEventRepository = instituteEventRepository;
        this.schoolRepository = schoolRepository;
        this.schoolHistoryService = schoolHistoryService;
    }

    /**
     * Process event.
     *
     * @param event the event
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processEvent(final GradSchool gradSchool, final InstituteEvent event) {
        log.info("Received and processing event: " + event.getEventId());

        var optSchool = schoolRepository.findById(gradSchool.getSchoolID());
        if(optSchool.isPresent()) {
            var school = optSchool.get();
            school.setCanIssueTranscripts(StringUtils.isNotBlank(gradSchool.getCanIssueTranscripts()) && gradSchool.getCanIssueTranscripts().equalsIgnoreCase("Y"));
            school.setCanIssueCertificates(StringUtils.isNotBlank(gradSchool.getCanIssueCertificates()) && gradSchool.getCanIssueCertificates().equalsIgnoreCase("Y"));
            school.setUpdateDate(LocalDateTime.now());
            school.setUpdateUser(gradSchool.getUpdateUser());
            TransformUtil.uppercaseFields(school); // convert the input to upper case.
            schoolRepository.save(school);
        }
        this.updateEvent(event);
    }

    /**
     * Gets event type.
     *
     * @return the event type
     */
    @Override
    public String getEventType() {
        return EventType.UPDATE_GRAD_SCHOOL.toString();
    }

}
