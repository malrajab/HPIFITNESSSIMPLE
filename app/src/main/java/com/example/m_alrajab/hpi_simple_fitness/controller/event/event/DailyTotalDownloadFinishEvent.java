package com.example.m_alrajab.hpi_simple_fitness.controller.event.event;

import java.util.Map;

/**
 * Created by m_alrajab on 21.12.16.
 */

public class DailyTotalDownloadFinishEvent {
    private Map<String, String> mDailyValueMap;

    public DailyTotalDownloadFinishEvent(Map<String, String> dailyValueMap) {
        mDailyValueMap = dailyValueMap;
    }

    public Map<String, String> getDailyValueMap() {
        return mDailyValueMap;
    }
}
