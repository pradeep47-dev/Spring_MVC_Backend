package com.siteactivity.reportingservice.model;

/*
 * Hit Counter will handle large Number of hits per second and UnOrdered hits
 * Data Structure has constant space - duration and overwrites past activity
 * If the duration increases or number of hits increase, datatype can be change to long
 * To handle concurrent requests
 *  - Distribute the counter
 *  - When a single machine gets too many traffic and performance becomes an issue, implement
 *    distributed solution.
 *  - Distributed system significantly reduces the burden of a single machine by scaling the
 *    system to multiple nodes, but at the same time adding complexity.
 */

public class HitCounter {

    private int[] timestamps;
    private int[] hits;
    private int duration;
    public HitCounter(Integer limit) {
        duration = limit; // 12hrs to seconds
        timestamps = new int[duration];
        hits = new int[duration];
    }

    public void hit(int timestamp, int value) {
        int i = timestamp % duration;
        if (timestamps[i] != timestamp) {
            timestamps[i] = timestamp;
            hits[i] = value;
        }
        else {
            hits[i] += value;
        }
    }

    public int getHits(int timestamp) {
        int value = 0;
        for (int i = 0; i < hits.length; i++) {
            if (timestamp - timestamps[i] < duration){
                value += hits[i];
            }
        }
        return value;
    }
}
