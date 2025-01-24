package ca.bc.gov.educ.api.institute.filter;

import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class SchoolFilterSpecs extends BaseFilterSpecs<SchoolEntity> {

  public SchoolFilterSpecs(FilterSpecifications<SchoolEntity, ChronoLocalDate> dateFilterSpecifications, FilterSpecifications<SchoolEntity, ChronoLocalDateTime<?>> dateTimeFilterSpecifications, FilterSpecifications<SchoolEntity, Integer> integerFilterSpecifications, FilterSpecifications<SchoolEntity, String> stringFilterSpecifications, FilterSpecifications<SchoolEntity, Long> longFilterSpecifications, FilterSpecifications<SchoolEntity, UUID> uuidFilterSpecifications, FilterSpecifications<SchoolEntity, Boolean> booleanFilterSpecifications, Converters converters) {
    super(dateFilterSpecifications, dateTimeFilterSpecifications, integerFilterSpecifications, stringFilterSpecifications, longFilterSpecifications, uuidFilterSpecifications, booleanFilterSpecifications, converters);
  }
}
