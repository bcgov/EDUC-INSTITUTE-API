package ca.bc.gov.educ.api.institute.filter;

import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class DistrictFilterSpecs extends BaseFilterSpecs<DistrictEntity> {

  public DistrictFilterSpecs(FilterSpecifications<DistrictEntity, ChronoLocalDate> dateFilterSpecifications, FilterSpecifications<DistrictEntity, ChronoLocalDateTime<?>> dateTimeFilterSpecifications, FilterSpecifications<DistrictEntity, Integer> integerFilterSpecifications, FilterSpecifications<DistrictEntity, String> stringFilterSpecifications, FilterSpecifications<DistrictEntity, Long> longFilterSpecifications, FilterSpecifications<DistrictEntity, UUID> uuidFilterSpecifications, Converters converters) {
    super(dateFilterSpecifications, dateTimeFilterSpecifications, integerFilterSpecifications, stringFilterSpecifications, longFilterSpecifications, uuidFilterSpecifications, converters);
  }
}
