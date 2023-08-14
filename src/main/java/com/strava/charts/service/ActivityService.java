package com.strava.charts.service;

import com.strava.charts.model.DailyProgressModel;
import com.strava.charts.model.DailyRideRecord;
import com.strava.charts.util.ActivityUtil;
import io.swagger.client.ApiException;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.model.SummaryActivity;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

@Service
@Slf4j
public class ActivityService {

     private final DecimalFormat decimalFormat = new DecimalFormat("#####.00");
     private static final List<String> DISTANCE_INTERVALS = Arrays.asList("-60", "60-90",
             "90-120", "120-150", "150-180", "+180");

     /**
      * Return all athlete's activities of a month determined by a reference date
      *
      * @param activitiesApi
      * @param referenceDate
      * @return
      */
     public List<SummaryActivity> getMonthYearActivities(ActivitiesApi activitiesApi,
             Long referenceDate, boolean monthly) {

          final List<SummaryActivity> activities = new ArrayList<>();

          final List<Long> epochs = ActivityUtil.getAfterBeforeDate(referenceDate,
                  monthly);

          final Integer beforeEpoch = epochs.get(1).intValue();
          final Integer afterEpoch = epochs.get(0).intValue();

          try {
               activities.addAll(
                       activitiesApi.getLoggedInAthleteActivities(beforeEpoch, afterEpoch,
                               1, 200));
               log.info("found {} activities", activities.size());
          } catch (ApiException ex) {
               log.error("Error getting athlete activities", ex);
          }
          return activities;
     }

     /**
      * @param activities
      * @return
      */
     public DailyProgressModel buildDailyDataPoints(Set<SummaryActivity> activities) {

          Map<Integer, Double> distanceByDay = new HashMap<>();
          Map<Integer, Integer> movingTimeByDay = new HashMap<>();
          Map<Integer, Integer> elapsedTimeByDay = new HashMap<>();

          OffsetDateTime activityDateTime = activities.stream().findFirst().get()
                  .getStartDateLocal();
          activities.forEach(activity -> {
               final int dayOfMonth = activity.getStartDateLocal().getDayOfMonth();
               distanceByDay.computeIfAbsent(dayOfMonth, k -> 0.0);
               movingTimeByDay.computeIfAbsent(dayOfMonth, k -> 0);
               elapsedTimeByDay.computeIfAbsent(dayOfMonth, k -> 0);

               final Double existingDistance = distanceByDay.get(dayOfMonth);
               final Integer existingMovingTime = movingTimeByDay.get(dayOfMonth);
               final Integer existingElapsedTime = elapsedTimeByDay.get(dayOfMonth);

               distanceByDay.put(dayOfMonth,
                       (activity.getDistance() + existingDistance) / 1000);
               movingTimeByDay.put(dayOfMonth,
                       (activity.getMovingTime() + existingMovingTime));
               elapsedTimeByDay.put(dayOfMonth,
                       (activity.getElapsedTime() + existingElapsedTime));
          });

          // add days with zero activity
          final List<Integer> daysOfActivityMonth = getDaysOfMonth(activityDateTime);
          daysOfActivityMonth.forEach(key -> {
               if (!distanceByDay.keySet().contains(key)) {
                    distanceByDay.put(key, 0.0);
               }

               if (!movingTimeByDay.keySet().contains(key)) {
                    movingTimeByDay.put(key, 0);
               }

               if (!elapsedTimeByDay.keySet().contains(key)) {
                    elapsedTimeByDay.put(key, 0);
               }
          });

          final List<Integer> keys = new ArrayList<>(distanceByDay.keySet());
          Collections.sort(keys);

          List<DailyRideRecord> dailyDailyRideRecords = new ArrayList<>();
          for (Integer key : keys) {
               final Double distance = distanceByDay.get(key);
               final Integer movingTime = movingTimeByDay.get(key);
               final Integer elapsedTime = elapsedTimeByDay.get(key);
               final Integer stoppedTime = elapsedTime - movingTime;
               final Double relativeMovingTime =
                       distance == 0 ? 0 : (movingTime * distance / elapsedTime);
               final Double relativeStoppedTime =
                       distance == 0 ? 0 : (stoppedTime * distance / elapsedTime);
               DailyRideRecord dailyRideRecord = new DailyRideRecord(key,
                       decimalFormat.format(distance), decimalFormat.format(movingTime),
                       decimalFormat.format(elapsedTime),
                       decimalFormat.format(stoppedTime),
                       decimalFormat.format(relativeMovingTime),
                       decimalFormat.format(relativeStoppedTime));
               dailyDailyRideRecords.add(dailyRideRecord);
          }

          DailyProgressModel model = new DailyProgressModel();
          model.getDailyRideRecords().addAll(dailyDailyRideRecords);

          return model;
     }

     /**
      * @param activities
      * @return
      */
     public Map<String, List<SummaryActivity>> buildRidesByDistanceMap(
             Set<SummaryActivity> activities) {
          Map<String, List<SummaryActivity>> result = new HashMap<>();
          activities.forEach(activity -> {
               final Float distance = activity.getDistance() / 1000;
               String key = "";
               if (distance < 60) {
                    key = "-60";
               } else if (distance >= 60 && distance < 90) {
                    key = "60-90";
               } else if (distance >= 90 && distance < 120) {
                    key = "90-120";
               } else if (distance >= 120 && distance < 150) {
                    key = "120-150";
               } else if (distance >= 150 && distance < 180) {
                    key = "150-180";
               } else if (distance >= 180) {
                    key = "+1800";
               }
               addToMap(result, key, activity);
          });

          return result;
     }

     private void addToMap(Map<String, List<SummaryActivity>> result, String key,
             SummaryActivity activity) {
          List<SummaryActivity> _activities = result.get(key);
          if (_activities == null) {
               _activities = new ArrayList<>();
               result.put(key, _activities);
          }
          _activities.add(activity);
     }

     private List<Integer> getDaysOfMonth(OffsetDateTime time) {
          boolean leapYear = time.getYear() % 4 == 0;
          final int monthLength = time.getMonth().length(leapYear);
          List<Integer> daysOfMonth = new ArrayList<>();
          for (int i = 1; i <= monthLength; i++) {
               daysOfMonth.add(i);
          }
          return daysOfMonth;

     }

}
