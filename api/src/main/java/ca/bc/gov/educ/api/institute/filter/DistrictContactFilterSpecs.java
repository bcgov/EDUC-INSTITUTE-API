package ca.bc.gov.educ.api.institute.filter;

import ca.bc.gov.educ.api.institute.model.v1.DistrictContactTombstoneEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class DistrictContactFilterSpecs extends BaseFilterSpecs<DistrictContactTombstoneEntity> {

  public DistrictContactFilterSpecs(FilterSpecifications<DistrictContactTombstoneEntity, ChronoLocalDate> dateFilterSpecifications, FilterSpecifications<DistrictContactTombstoneEntity, ChronoLocalDateTime<?>> dateTimeFilterSpecifications, FilterSpecifications<DistrictContactTombstoneEntity, Integer> integerFilterSpecifications, FilterSpecifications<DistrictContactTombstoneEntity, String> stringFilterSpecifications, FilterSpecifications<DistrictContactTombstoneEntity, Long> longFilterSpecifications, FilterSpecifications<DistrictContactTombstoneEntity, UUID> uuidFilterSpecifications, FilterSpecifications<DistrictContactTombstoneEntity, Boolean> booleanFilterSpecifications, Converters converters) {
    super(dateFilterSpecifications, dateTimeFilterSpecifications, integerFilterSpecifications, stringFilterSpecifications, longFilterSpecifications, uuidFilterSpecifications, booleanFilterSpecifications, converters);
  }
}
