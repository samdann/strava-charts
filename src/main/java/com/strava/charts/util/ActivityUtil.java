package com.strava.charts.util;

import io.swagger.client.model.ActivityType;
import io.swagger.client.model.SummaryActivity;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class ActivityUtil {

    public static Map<ActivityType, Set<SummaryActivity>> groupActivitiesByType(List<SummaryActivity> activities) {
        Map<ActivityType, Set<SummaryActivity>> activitiesByType = new HashMap<>();
        activities.forEach(activity -> {
            Set<SummaryActivity> subActivities = activitiesByType.get(activity.getType());
            if (subActivities == null) {
                subActivities = new HashSet<>();
                activitiesByType.putIfAbsent(activity.getType(), subActivities);
            }
            subActivities.add(activity);
        });

        return activitiesByType;
    }
}
