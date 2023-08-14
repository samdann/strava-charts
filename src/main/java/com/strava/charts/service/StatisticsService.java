package com.strava.charts.service;

import com.strava.charts.util.ActivityUtil;
import io.swagger.client.ApiException;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.model.ActivityType;
import io.swagger.client.model.SummaryActivity;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StatisticsService {

     /**
      * @param code type of activity for which the max heart rate is to be calculated
      * @return the max heart rate number for the given input
      */
     public int getMaxHearRateByActivityType(final ActivitiesApi activitiesApi,
             final String code) throws ApiException {
          final Long referenceDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
          final List<Long> epochs = ActivityUtil.getAfterBeforeDate(referenceDate,
                  Boolean.FALSE);
          final Integer beforeEpoch = epochs.get(1).intValue();
          final Integer afterEpoch = epochs.get(0).intValue();

          List<Long> activityIds = activitiesApi.getLoggedInAthleteActivities(beforeEpoch,
                          afterEpoch, 1, 200).stream()
                  .filter(activity -> ActivityType.RUN.equals(activity.getType()))
                  .map(SummaryActivity::getId).collect(Collectors.toList());

          log.info("for the given time, there are {} running activities",
                  activityIds.size());

          final List<Integer> maxHeartRateList = new ArrayList<>();
          activityIds.forEach(id -> {
               try {
                    Integer max = activitiesApi.getActivityById(id, Boolean.TRUE)
                            .getSegmentEfforts().stream()
                            .map(detailedSegmentEffort -> detailedSegmentEffort.getMaxHeartrate()
                                    .intValue()).max(Integer::compareTo).orElse(0);

                    maxHeartRateList.add(max);

               } catch (ApiException e) {
                    throw new RuntimeException(e);
               }
          });
          maxHeartRateList.sort(Collections.reverseOrder());
          log.info("Max heart rate for all activities is {}", maxHeartRateList.get(0));
          return maxHeartRateList.get(0);
     }
}
