package ca.bc.gov.educ.api.institute.filter;

import ca.bc.gov.educ.api.institute.model.v1.DistrictContactEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class DistrictContactFilterSpecs extends BaseFilterSpecs<DistrictContactEntity> {

  public DistrictContactFilterSpecs(FilterSpecifications<DistrictContactEntity, ChronoLocalDate> dateFilterSpecifications, FilterSpecifications<DistrictContactEntity, ChronoLocalDateTime<?>> dateTimeFilterSpecifications, FilterSpecifications<DistrictContactEntity, Integer> integerFilterSpecifications, FilterSpecifications<DistrictContactEntity, String> stringFilterSpecifications, FilterSpecifications<DistrictContactEntity, Long> longFilterSpecifications, FilterSpecifications<DistrictContactEntity, UUID> uuidFilterSpecifications, Converters converters) {
    super(dateFilterSpecifications, dateTimeFilterSpecifications, integerFilterSpecifications, stringFilterSpecifications, longFilterSpecifications, uuidFilterSpecifications, converters);
  }
}
