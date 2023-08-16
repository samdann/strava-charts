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
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActivityService {

     private static final int STRAVA_API_LIMIT = 600;
     private static final long DELAY_TIME = 1500;

     @Autowired
     ActivityRepository activityRepository;

     /**
      * @param client ApiClient
      */
     public void importActivities(final ApiClient client) {
          log.info("Importing all activities...");

          final ActivitiesApi activitiesApi = new ActivitiesApi(client);
          final AthletesApi athletesApi = new AthletesApi(client);

          final Map<Long, Activity> activityById = new HashMap<>();

          final Integer beforeEpoch = Long.valueOf(
                  LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).intValue();
          Integer afterEpoch;
          try {
               afterEpoch = (int) athletesApi.getLoggedInAthlete().getCreatedAt()
                       .toEpochSecond();
          } catch (ApiException ex) {
               log.error("Error getting logged athlete : {}", ex.getMessage());
               afterEpoch = getDefaultDate();
          }

          final List<Activity> activities = getAllActivities(activitiesApi, beforeEpoch,
                  afterEpoch);

          activities.forEach(activity -> {
               activityById.putIfAbsent(activity.getId(), activity);
          });

          //Enrich the activities with heart rate data
          enrichActivityData(activities, activitiesApi, activityById);
     }

     /**
      * @param activityIds list of activity ids to flag
      */
     public void flagCorruptedActivities(List<String> activityIds) {
          final List<Activity> activities = new ArrayList<>();

          activityRepository.findAllById(activityIds).forEach(activities::add);
          activities.forEach(activity -> activity.setFaultySensorData(true));

          activityRepository.saveAll(activities);

          log.info("Flagged {} activities as corrupted: i.e faultySensorData = true",
                  activities.size());
     }

     private List<Activity> getAllActivities(final ActivitiesApi activitiesApi,
             final Integer beforeEpoch, final Integer afterEpoch) {

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
                    List<SummaryActivity> activities = null;
                    try {
                         activities = activitiesApi.getLoggedInAthleteActivities(
                                 beforeEpoch, (int) latestTimestamp, pageNumber,
                                 itemsPerPage);
                    } catch (ApiException ex) {
                         log.error("Error getting  athlete activities: {}",
                                 ex.getMessage());
                         break;
                    }
                    activityList.addAll(
                            activities.stream().map(Activity::convertToActivity)
                                    .collect(Collectors.toList()));
                    pageNumber++;
                    hasMoreItems = activities.size() == itemsPerPage;
               }

               activityRepository.saveAll(activityList);
               log.info("{} activities have been imported", activityList.size());

          }

          return activityList;
     }

     private boolean fetchLatestData(final ActivitiesApi activitiesApi,
             final Integer beforeEpoch, final Integer afterEpoch) {

          LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(beforeEpoch),
                  TimeZone.getDefault().toZoneId());
          log.info("Checking for activities after {}", time);

          List<SummaryActivity> latestActivities = null;
          try {
               latestActivities = activitiesApi.getLoggedInAthleteActivities(beforeEpoch,
                       afterEpoch, 1, 1);
          } catch (ApiException ex) {
               log.error("Error getting  athlete activities: {}", ex.getMessage());
               return false;
          }
          return latestActivities.size() > 0;

     }

     private void enrichActivityData(final List<Activity> activities,
             final ActivitiesApi activitiesApi, final Map<Long, Activity> activityById) {

          List<Long> activityIds = activities.stream().filter(activity ->
                          (ActivityType.RUN.equals(activity.getType())
                                  || ActivityType.RIDE.equals(activity.getType()))
                                  && activity.getMaxHeartRate() == null
                                  && activity.getUpdated() == null).map(Activity::getId)
                  .collect(Collectors.toList());

          log.info("{} activities (Ride/Run) will be enriched with max heart rate data",
                  activityIds.size());

          boolean throttle = activityIds.size() > STRAVA_API_LIMIT;
          activityIds.forEach(id -> {
               try {
                    Thread.sleep(throttle ? DELAY_TIME : 150);
                    addMaxHearRate(activitiesApi, id, activityById);
               } catch (InterruptedException e) {
                    throw new RuntimeException(e);
               }
          });
     }

     private void addMaxHearRate(final ActivitiesApi activitiesApi, final Long id,
             final Map<Long, Activity> activityById) {
          int hr = 0;
          try {
               hr = activitiesApi.getActivityById(id, Boolean.TRUE).getSegmentEfforts()
                       .stream().map(detailedSegmentEffort ->
                               detailedSegmentEffort.getMaxHeartrate() != null
                                       ? detailedSegmentEffort.getMaxHeartrate()
                                       .intValue() : 0).max(Integer::compareTo).orElse(0);
          } catch (ApiException ex) {
               log.error("Error getting activity detailed: {}", ex.getMessage());
               return;
          }

          //persisting the data
          Activity activity = activityById.get(id);
          activity.setMaxHeartRate(hr);
          activity.setUpdated(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
          activityRepository.save(activity);

          log.info("Activity with id {} has a max heart rate of {}", id, hr);
     }

     private Integer getDefaultDate() {

          return (int) LocalDateTime.of(LocalDateTime.now().getYear(), Month.JANUARY, 1,
                  0, 0, 0, 0).toEpochSecond(ZoneOffset.UTC);
     }

}
