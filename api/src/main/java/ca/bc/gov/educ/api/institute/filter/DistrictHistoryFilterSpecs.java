package ca.bc.gov.educ.api.institute.filter;

import ca.bc.gov.educ.api.institute.model.v1.DistrictHistoryEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class DistrictHistoryFilterSpecs extends BaseFilterSpecs<DistrictHistoryEntity> {

  public DistrictHistoryFilterSpecs(FilterSpecifications<DistrictHistoryEntity, ChronoLocalDate> dateFilterSpecifications, FilterSpecifications<DistrictHistoryEntity, ChronoLocalDateTime<?>> dateTimeFilterSpecifications, FilterSpecifications<DistrictHistoryEntity, Integer> integerFilterSpecifications, FilterSpecifications<DistrictHistoryEntity, String> stringFilterSpecifications, FilterSpecifications<DistrictHistoryEntity, Long> longFilterSpecifications, FilterSpecifications<DistrictHistoryEntity, UUID> uuidFilterSpecifications, FilterSpecifications<DistrictHistoryEntity, Boolean> booleanFilterSpecifications, Converters converters) {
    super(dateFilterSpecifications, dateTimeFilterSpecifications, integerFilterSpecifications, stringFilterSpecifications, longFilterSpecifications, uuidFilterSpecifications, booleanFilterSpecifications, converters);
  }
}
