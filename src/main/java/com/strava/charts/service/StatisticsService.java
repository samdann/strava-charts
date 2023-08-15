package com.strava.charts.service;

import com.strava.charts.repository.ActivityRepository;
import io.swagger.client.ApiException;
import io.swagger.client.api.ActivitiesApi;
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

          return 0;
     }

}
