package ca.bc.gov.educ.api.institute.filter;

import ca.bc.gov.educ.api.institute.model.v1.SchoolContactTombstoneEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class SchoolContactFilterSpecs extends BaseFilterSpecs<SchoolContactTombstoneEntity> {

  public SchoolContactFilterSpecs(FilterSpecifications<SchoolContactTombstoneEntity, ChronoLocalDate> dateFilterSpecifications, FilterSpecifications<SchoolContactTombstoneEntity, ChronoLocalDateTime<?>> dateTimeFilterSpecifications, FilterSpecifications<SchoolContactTombstoneEntity, Integer> integerFilterSpecifications, FilterSpecifications<SchoolContactTombstoneEntity, String> stringFilterSpecifications, FilterSpecifications<SchoolContactTombstoneEntity, Long> longFilterSpecifications, FilterSpecifications<SchoolContactTombstoneEntity, UUID> uuidFilterSpecifications, Converters converters) {
    super(dateFilterSpecifications, dateTimeFilterSpecifications, integerFilterSpecifications, stringFilterSpecifications, longFilterSpecifications, uuidFilterSpecifications, converters);
  }
}
