package ca.bc.gov.educ.api.institute.filter;

import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class AuthorityFilterSpecs extends BaseFilterSpecs<IndependentAuthorityEntity> {

  public AuthorityFilterSpecs(FilterSpecifications<IndependentAuthorityEntity, ChronoLocalDate> dateFilterSpecifications, FilterSpecifications<IndependentAuthorityEntity, ChronoLocalDateTime<?>> dateTimeFilterSpecifications, FilterSpecifications<IndependentAuthorityEntity, Integer> integerFilterSpecifications, FilterSpecifications<IndependentAuthorityEntity, String> stringFilterSpecifications, FilterSpecifications<IndependentAuthorityEntity, Long> longFilterSpecifications, FilterSpecifications<IndependentAuthorityEntity, UUID> uuidFilterSpecifications, FilterSpecifications<IndependentAuthorityEntity, Boolean> booleanFilterSpecifications, Converters converters) {
    super(dateFilterSpecifications, dateTimeFilterSpecifications, integerFilterSpecifications, stringFilterSpecifications, longFilterSpecifications, uuidFilterSpecifications, booleanFilterSpecifications, converters);
  }
}
