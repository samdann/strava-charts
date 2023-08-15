package com.strava.charts.controller;

import com.strava.charts.service.AuthorizationService;
import com.strava.charts.service.StatisticsService;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.api.AthletesApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.threeten.bp.OffsetDateTime;

@Slf4j
@Validated
@Timed
@RestController
public class StatisticsController {

     @Autowired
     AuthorizationService authorizationService;

     @Autowired
     StatisticsService statisticsService;

     @ApiOperation(value = "getMaxHeartRate", tags = "statistics-api")
     @RequestMapping(value = "/_get-max-heart-rate", produces = {
             "application/json"}, method = RequestMethod.GET)
     public int getMaxHeartRate(
             @ApiParam(name = "code", value = "authorization code") @RequestParam(value = "code") String code)
             throws ApiException {

          ApiClient client = authorizationService.getAuthorizedApiClient(code);

          final ActivitiesApi activitiesApi = new ActivitiesApi(client);
          final AthletesApi athletesApi = new AthletesApi(client);

          OffsetDateTime createdAt = athletesApi.getLoggedInAthlete().getCreatedAt();
          return statisticsService.getMaxHearRateByActivityType(activitiesApi, createdAt);

     }

}
