package com.strava.charts.service;

import com.strava.charts.model.primary.Activity;
import com.strava.charts.repository.ActivityRepository;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.api.AthletesApi;
import io.swagger.client.model.ActivityType;
import io.swagger.client.model.SummaryActivity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActivityService {

     @Autowired
     ActivityRepository activityRepository;

     @SneakyThrows
     public Integer importActivities(final ApiClient client) {
          log.info("Importing all activities...");

          final ActivitiesApi activitiesApi = new ActivitiesApi(client);
          final AthletesApi athletesApi = new AthletesApi(client);

          final Map<Long, Activity> activityById = new HashMap<>();

          final Integer beforeEpoch = Long.valueOf(
                  LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).intValue();
          final Integer afterEpoch = (int) athletesApi.getLoggedInAthlete().getCreatedAt()
                  .toEpochSecond();

          final List<Activity> activities = getAllActivities(activitiesApi, beforeEpoch,
                  afterEpoch);
          activities.forEach(activity -> {
               activityById.putIfAbsent(activity.getId(), activity);
               activityRepository.save(activity);
          });

          List<Long> activityIds = activities.stream()
                  .filter(activity -> ActivityType.RUN.equals(activity.getType()))
                  .map(Activity::getId).collect(Collectors.toList());

          log.info("for the given time, there are {} running activities",
                  activityIds.size());

          activityIds.forEach(id -> {
               try {
                    Integer hr = activitiesApi.getActivityById(id, Boolean.TRUE)
                            .getSegmentEfforts().stream().map(detailedSegmentEffort ->
                                    detailedSegmentEffort.getMaxHeartrate() != null
                                            ? detailedSegmentEffort.getMaxHeartrate()
                                            .intValue() : 0).max(Integer::compareTo)
                            .orElse(0);

                    //persisting the data
                    Activity activity = activityById.get(id);
                    activity.setMaxHeartRate(hr);
                    activityRepository.save(activity);

                    log.info("Activity with id {} has a max heart rate of {}", id, hr);

               } catch (ApiException e) {
                    throw new RuntimeException(e);
               }
          });

          return 0;
     }

     private List<Activity> getAllActivities(final ActivitiesApi activitiesApi,
             final Integer beforeEpoch, final Integer afterEpoch) throws ApiException {

          //check data in db first
          final List<Activity> activityList = activityRepository.findAll();

          //get recent activity creation time
          Optional<Long> max = activityList.stream().map(Activity::getCreated)
                  .max(Long::compareTo);
          long latestTimestamp = max.isPresent() ? max.get() : afterEpoch;

          // true
          boolean fetchLatestData = fetchLatestData(activitiesApi, beforeEpoch,
                  (int) latestTimestamp);

          if (fetchLatestData) {

               boolean hasMoreItems = true;
               int pageNumber = 1;
               int itemsPerPage = 30;
               while (hasMoreItems) {
                    log.info("Retrieving {} items at page {}", itemsPerPage, pageNumber);
                    List<SummaryActivity> activities = activitiesApi.getLoggedInAthleteActivities(
                            beforeEpoch, (int) latestTimestamp, pageNumber, itemsPerPage);
                    activityList.addAll(
                            activities.stream().map(Activity::convertToActivity)
                                    .collect(Collectors.toList()));
                    pageNumber++;
                    hasMoreItems = activities.size() == itemsPerPage;
               }
               log.info("retrieved {} activities...", activityList.size());
          }

          return activityList;
     }

     @SneakyThrows
     private boolean fetchLatestData(final ActivitiesApi activitiesApi,
             final Integer beforeEpoch, final Integer afterEpoch) {

          LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(beforeEpoch),
                  TimeZone.getDefault().toZoneId());
          log.info("Checking for activities after {}", time);

          List<SummaryActivity> latestActivities = activitiesApi.getLoggedInAthleteActivities(
                  beforeEpoch, afterEpoch, 1, 1);
          return latestActivities.size() > 0;

     }

}
