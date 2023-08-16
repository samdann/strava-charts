package com.strava.charts.service;

import com.strava.charts.model.primary.Activity;
import com.strava.charts.repository.ActivityRepository;
import io.swagger.client.model.ActivityType;
import java.util.Comparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StatisticsService {

     @Autowired
     ActivityRepository activityRepository;

     /**
      * @return the max heart rate number for the given input
      */
     public Activity getMaxHearRateByActivityType() {

          return activityRepository.findAll().stream()
                  .filter(activity -> ActivityType.RUN.equals(activity.getType()) && (
                          activity.getFaultySensorData() == null
                                  || !activity.getFaultySensorData()))
                  .max(Comparator.comparingInt(Activity::getMaxHeartRate)).orElse(null);

     }

}
