package com.strava.charts.service;

import com.strava.charts.model.primary.Activity;
import com.strava.charts.repository.ActivityRepository;
import io.swagger.client.model.ActivityType;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StatisticsService {

     @Autowired
     ActivityRepository activityRepository;

     /**
      * @param activitiesApi native API
      * @param createdAt     date of creation
      * @return the max heart rate number for the given input
      */
     public int getMaxHearRateByActivityType() {

          Optional<Integer> max = activityRepository.findAll().stream()
                  .filter(activity -> ActivityType.RUN.equals(activity.getType()))
                  .map(Activity::getMaxHeartRate).max(Integer::compareTo);

          return max.orElse(0);
     }

}
