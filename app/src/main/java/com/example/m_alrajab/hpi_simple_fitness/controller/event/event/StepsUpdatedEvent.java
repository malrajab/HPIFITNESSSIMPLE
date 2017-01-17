package com.example.m_alrajab.hpi_simple_fitness.controller.event.event;

/**
 * Created by m_alrajab on 21.12.16.
 */
public class StepsUpdatedEvent {
    private String mStepCount;

    public StepsUpdatedEvent(String value) {
        mStepCount = value;
    }

    public String getStepCount() {
        return mStepCount;
    }
}
