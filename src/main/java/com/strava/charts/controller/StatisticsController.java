package com.strava.charts.controller;

import com.strava.charts.service.AuthorizationService;
import com.strava.charts.service.StatisticsService;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.client.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

          return statisticsService.getMaxHearRateByActivityType();

     }

}
