package ca.bc.gov.educ.api.institute.filter;

import ca.bc.gov.educ.api.institute.model.v1.SchoolHistoryEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class SchoolHistoryFilterSpecs extends BaseFilterSpecs<SchoolHistoryEntity> {

  public SchoolHistoryFilterSpecs(FilterSpecifications<SchoolHistoryEntity, ChronoLocalDate> dateFilterSpecifications, FilterSpecifications<SchoolHistoryEntity, ChronoLocalDateTime<?>> dateTimeFilterSpecifications, FilterSpecifications<SchoolHistoryEntity, Integer> integerFilterSpecifications, FilterSpecifications<SchoolHistoryEntity, String> stringFilterSpecifications, FilterSpecifications<SchoolHistoryEntity, Long> longFilterSpecifications, FilterSpecifications<SchoolHistoryEntity, UUID> uuidFilterSpecifications, Converters converters) {
    super(dateFilterSpecifications, dateTimeFilterSpecifications, integerFilterSpecifications, stringFilterSpecifications, longFilterSpecifications, uuidFilterSpecifications, converters);
  }
}
