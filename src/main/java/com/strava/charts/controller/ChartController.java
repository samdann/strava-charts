package com.strava.charts.controller;

import com.strava.charts.model.DailyProgressModel;
import com.strava.charts.model.RideByDistanceRecord;
import com.strava.charts.service.AuthorizationService;
import com.strava.charts.service.ChartService;
import com.strava.charts.util.ActivityUtil;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.client.ApiClient;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.model.ActivityType;
import io.swagger.client.model.SummaryActivity;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class ChartController {

     @Autowired
     AuthorizationService authorisationService;

     @Autowired
     ChartService chartService;

     @ApiOperation(value = "generateActivityChart", tags = "activity-api")
     @RequestMapping(value = "/_generate-activity-chart", produces = {
             "application/json"}, method = RequestMethod.GET)
     public ResponseEntity<DailyProgressModel> generateActivityChart(
             @ApiParam(name = "code", value = "authorization code") @RequestParam(value = "code") String code,
             @ApiParam(name = "referenceDate", value = "reference date", example = "159873833") @RequestParam(value = "referenceDate", required = false) String referenceDate) {

          final List<SummaryActivity> activities = getAthleteActivities(code,
                  referenceDate, Boolean.TRUE);
          final Map<ActivityType, Set<SummaryActivity>> activitiesByType = ActivityUtil.groupActivitiesByType(
                  activities);

          DailyProgressModel dailyProgressModel = null;
          for (ActivityType type : activitiesByType.keySet()) {
               if (ActivityType.RIDE.equals(type)) {
                    dailyProgressModel = chartService.buildDailyDataPoints(
                            activitiesByType.get(type));
               }
          }

          return ResponseEntity.ok().body(dailyProgressModel);
     }

     @ApiOperation(value = "movingByElapsedTimeChart", tags = "activity-api")
     @RequestMapping(value = "/_moving-by-elapsed-time-chart", produces = {
             "application/json"}, method = RequestMethod.GET)
     public ResponseEntity<DailyProgressModel> movingByElapsedTimeChart(
             @ApiParam(name = "code", value = "authorization code") @RequestParam(value = "code") String code,
             @ApiParam(name = "referenceDate", value = "reference date", example = "159873833") @RequestParam(value = "referenceDate", required = false) String referenceDate) {

          final List<SummaryActivity> activities = getAthleteActivities(code,
                  referenceDate, Boolean.TRUE);
          final Map<ActivityType, Set<SummaryActivity>> activitiesByType = ActivityUtil.groupActivitiesByType(
                  activities);
          final DailyProgressModel dailyProgressModel = chartService.buildDailyDataPoints(
                  activitiesByType.get(ActivityType.RIDE));

          final Map<String, List<SummaryActivity>> ridesByDistanceRecord = getRidesByDistanceRecord(
                  code, referenceDate);
          final List<RideByDistanceRecord> records = new ArrayList<>();
          ridesByDistanceRecord.forEach((key, value) -> {
               RideByDistanceRecord record = new RideByDistanceRecord();
               record.setGroupLabel(key);
               record.setCount(value.size());
               records.add(record);
          });

          dailyProgressModel.setRideByDistanceRecords(records);
          return ResponseEntity.ok().body(dailyProgressModel);
     }

     @ApiOperation(value = "ridesByDistanceChart", tags = "activity-api")
     @RequestMapping(value = "/_rides-by-distance-chart", produces = {
             "application/json"}, method = RequestMethod.GET)
     public ResponseEntity<Map<String, List<SummaryActivity>>> getRidesByDistance(
             @ApiParam(name = "code", value = "authorization code") @RequestParam(value = "code") String code,
             @ApiParam(name = "referenceDate", value = "reference date", example = "159873833") @RequestParam(value = "referenceDate", required = false) String referenceDate) {

          final Map<String, List<SummaryActivity>> ridesByDistance = getRidesByDistanceRecord(
                  code, referenceDate);
          return ResponseEntity.ok().body(ridesByDistance);
     }

     private Map<String, List<SummaryActivity>> getRidesByDistanceRecord(String code,
             String referenceDate) {
          final List<SummaryActivity> activities = getAthleteActivities(code,
                  referenceDate, Boolean.FALSE);
          final Map<ActivityType, Set<SummaryActivity>> activitiesByType = ActivityUtil.groupActivitiesByType(
                  activities);
          final Set<SummaryActivity> ridingActivities = activitiesByType.get(
                  ActivityType.RIDE);
          return chartService.buildRidesByDistanceMap(ridingActivities);
     }

     private List<SummaryActivity> getAthleteActivities(String code, String referenceDate,
             boolean monthly) {

          ApiClient client = authorisationService.getAuthorizedApiClient(code);

          final ActivitiesApi activitiesApi = new ActivitiesApi(client);

          if (referenceDate == null || referenceDate.isEmpty()) {
               referenceDate = String.valueOf(
                       LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
          }
          return chartService.getMonthYearActivities(activitiesApi,
                  Long.valueOf(referenceDate), monthly);
     }

}
