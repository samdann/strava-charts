package com.strava.charts.util;

import io.swagger.client.model.ActivityType;
import io.swagger.client.model.SummaryActivity;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ActivityUtil {

     public static Map<ActivityType, Set<SummaryActivity>> groupActivitiesByType(
             List<SummaryActivity> activities) {
          Map<ActivityType, Set<SummaryActivity>> activitiesByType = new HashMap<>();
          activities.forEach(activity -> {
               Set<SummaryActivity> subActivities = activitiesByType.get(
                       activity.getType());
               if (subActivities == null) {
                    subActivities = new HashSet<>();
                    activitiesByType.putIfAbsent(activity.getType(), subActivities);
               }
               subActivities.add(activity);
          });

          return activitiesByType;
     }

     public static List<Long> getAfterBeforeDate(Long date, boolean monthly) {
          final LocalDateTime dateTime = LocalDateTime.ofEpochSecond(date, 0,
                  ZoneOffset.UTC);
          LocalDateTime after = LocalDateTime.of(dateTime.getYear(),
                  monthly ? dateTime.getMonth() : Month.JANUARY, 1, 0, 0, 0, 0);
          LocalDateTime before = monthly ? after.plusMonths(1) : after.plusYears(1);

          List<Long> result = new ArrayList<>();
          result.add(after.toEpochSecond(ZoneOffset.UTC));
          result.add(before.toEpochSecond(ZoneOffset.UTC));

          return result;
     }
}
