package com.mlieshoff.shaft.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.mlieshoff.shaft.service.LocationService;
import com.mlieshoff.shaft.service.LocationServiceDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.stream.Location;

@ExtendWith(MockitoExtension.class)
class LocationRestControllerTest {

  @InjectMocks private LocationRestController unitUnderTest;

  @Mock private LocationService locationService;

  @Test
  void create_whenCalled_thenReturnResults() {
    LocationRestDto expected = new LocationRestDto(42L, 815L);
    when(locationService.create("database", "bucket"))
        .thenReturn(new LocationServiceDto(42l, 815l));

    LocationRestDto actual = unitUnderTest.create("database", "bucket");

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }
}
