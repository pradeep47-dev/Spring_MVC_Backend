package com.siteactivity.reportingservice.model;

import com.siteactivity.reportingservice.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Activity implements ActivityService {

    private static final Logger LOG = LoggerFactory.getLogger(Activity.class);

    private final HashMap<String, HitCounter> activities = new HashMap<String, HitCounter>();

    public boolean postActivity(String key, int value) {

        if(activities.containsKey(key) == false) {
            activities.put(key, new HitCounter( 43200));
            return false;
        }
        activities.get(key).hit((int) (System.currentTimeMillis() / 1000), value);
        return true;
    }

    public String getActivity(String key) {
        if(activities.containsKey(key) == false) {
            return "No record";
        }
        try {
            int hitCount = activities.get(key).getHits((int) (System.currentTimeMillis() / 1000));
            return String.valueOf(hitCount);
        } catch (Exception exception) {
            LOG.error(exception.toString());
            return "Error";
        }
    }
}


