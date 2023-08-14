package com.strava.charts.service;

import com.strava.charts.model.primary.Activity;
import com.strava.charts.repository.ActivityRepository;
import io.swagger.client.ApiException;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.model.ActivityType;
import io.swagger.client.model.SummaryActivity;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

@Service
@Slf4j
public class StatisticsService {

     @Autowired
     ActivityRepository activityRepository;

     /**
      * @param activitiesApi native API
      * @param createdAt     date of creation
      * @return the max heart rate number for the given input
      * @throws ApiException native exception
      */
     public int getMaxHearRateByActivityType(final ActivitiesApi activitiesApi,
             final OffsetDateTime createdAt) throws ApiException {

          final Map<Long, Activity> activityById = new HashMap<>();

          final Integer beforeEpoch = Long.valueOf(
                  LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).intValue();
          final Integer afterEpoch = Long.valueOf(createdAt.toEpochSecond()).intValue();

          final List<SummaryActivity> summaryActivities = getAllActivities(activitiesApi,
                  beforeEpoch, afterEpoch);
          summaryActivities.forEach(sumActivity -> {
               Activity activity = Activity.builder().id(sumActivity.getId())
                       .type(sumActivity.getType())
                       .created(sumActivity.getStartDate().toEpochSecond()).build();
               activityById.putIfAbsent(activity.getId(), activity);
               activityRepository.save(activity);
          });

          List<Long> activityIds = summaryActivities.stream()
                  .filter(activity -> ActivityType.RUN.equals(activity.getType()))
                  .map(SummaryActivity::getId).collect(Collectors.toList());

          log.info("for the given time, there are {} running activities",
                  activityIds.size());

          final List<Integer> heartRateList = new ArrayList<>();
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

                    heartRateList.add(hr);
                    log.info("Activity with id {} has a max heart rate of {}", id, hr);

               } catch (ApiException e) {
                    throw new RuntimeException(e);
               }
          });
          heartRateList.sort(Collections.reverseOrder());
          log.info("Max heart rate for all activities is {}", heartRateList.get(0));
          return heartRateList.get(0);
     }

     private List<SummaryActivity> getAllActivities(final ActivitiesApi activitiesApi,
             final Integer beforeEpoch, final Integer afterEpoch) throws ApiException {
          //check data in db first
          List<Activity> activityList = activityRepository.findAll();

          // TODO who to determine if a new pull from the API is required

          boolean hasMoreItems = true;
          int pageNumber = 1;
          int itemsPerPage = 30;
          final List<SummaryActivity> result = new ArrayList<>();
          while (hasMoreItems) {
               log.info("Retrieving {} items at page {}", itemsPerPage, pageNumber);
               List<SummaryActivity> activities = activitiesApi.getLoggedInAthleteActivities(
                       beforeEpoch, afterEpoch, pageNumber, itemsPerPage);
               result.addAll(activities);
               pageNumber++;
               hasMoreItems = activities.size() == itemsPerPage;
          }
          log.info("retrieved {} activities", result.size());
          return result;
     }
}
