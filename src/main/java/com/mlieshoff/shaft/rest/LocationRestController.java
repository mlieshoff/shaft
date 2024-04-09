package com.mlieshoff.shaft.rest;

import com.mlieshoff.shaft.service.LocationService;
import com.mlieshoff.shaft.service.LocationServiceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationRestController {

  private final LocationService locationService;

  @Operation(summary = "Creates a location")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Location info",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = LocationRestDto.class))
            })
      })
  @PostMapping(value = "locations/create/{database}/{bucket}", produces = "application/json")
  @ResponseBody
  public LocationRestDto create(
      @PathVariable("database") @NotBlank(message = "'database' must be not blank") String database,
      @PathVariable("bucket") @NotBlank(message = "'bucket' must be not blank") String bucket) {
    log.info("database: {}, bucket: {}", database, bucket);
    LocationServiceDto locationServiceDto = locationService.create(database, bucket);
    return LocationRestMapper.INSTANCE.locationServiceDtoToLocationRestDto(locationServiceDto);
  }
}
