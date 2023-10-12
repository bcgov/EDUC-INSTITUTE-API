package ca.bc.gov.educ.api.institute.filter;

import ca.bc.gov.educ.api.institute.model.v1.SchoolContactEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class SchoolContactFilterSpecs extends BaseFilterSpecs<SchoolContactEntity> {

  public SchoolContactFilterSpecs(FilterSpecifications<SchoolContactEntity, ChronoLocalDate> dateFilterSpecifications, FilterSpecifications<SchoolContactEntity, ChronoLocalDateTime<?>> dateTimeFilterSpecifications, FilterSpecifications<SchoolContactEntity, Integer> integerFilterSpecifications, FilterSpecifications<SchoolContactEntity, String> stringFilterSpecifications, FilterSpecifications<SchoolContactEntity, Long> longFilterSpecifications, FilterSpecifications<SchoolContactEntity, UUID> uuidFilterSpecifications, Converters converters) {
    super(dateFilterSpecifications, dateTimeFilterSpecifications, integerFilterSpecifications, stringFilterSpecifications, longFilterSpecifications, uuidFilterSpecifications, converters);
  }
}
