package com.strava.charts.controller;

import com.strava.charts.repository.ActivityRepository;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@Timed
@RestController
public class AdminController {

     @Autowired
     ActivityRepository activityRepository;

     @ApiOperation(value = "deleteActivities", tags = "admin-api")
     @RequestMapping(value = "/_delete-all-activities", produces = {
             "application/json"}, method = RequestMethod.DELETE)
     public ResponseEntity<String> deleteAllActivities() {

          long count = activityRepository.count();
          activityRepository.deleteAll();
          String message = count + " activities have been successfully deleted";
          log.info(message);
          return ResponseEntity.ok().body(message);
     }

}
