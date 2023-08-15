package com.strava.charts.controller;

import com.strava.charts.service.ActivityService;
import com.strava.charts.service.AuthorizationService;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.client.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@Timed
@RestController
public class ActivityController {

     @Autowired
     AuthorizationService authorizationService;

     @Autowired
     ActivityService activityService;

     @ApiOperation(value = "importAllActivities", tags = "admin-api")
     @RequestMapping(value = "/_import-all-activities", produces = {
             "application/json"}, method = RequestMethod.POST)
     public ResponseEntity<String> importActivities(
             @ApiParam(name = "code", value = "authorization code") @RequestParam(value = "code", required = true) String code) {

          ApiClient client = authorizationService.getAuthorizedApiClient(code);
          activityService.importActivities(client);

          return ResponseEntity.ok().build();
     }
}
