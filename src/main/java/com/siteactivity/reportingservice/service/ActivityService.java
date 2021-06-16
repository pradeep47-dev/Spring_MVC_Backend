package com.siteactivity.reportingservice.service;

public interface ActivityService {
    boolean postActivity(String key, int hit);

    String getActivity(String key);
}
