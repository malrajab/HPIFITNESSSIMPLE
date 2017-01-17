package com.example.m_alrajab.hpi_simple_fitness.controller.event.event;

import com.google.android.gms.fitness.result.DataReadResult;

/**
 * Created by m_alrajab on 20.12.16.
 */

public class DataLoadFinishedEvent {
    private DataReadResult mDataReadResult;

    public DataLoadFinishedEvent(DataReadResult dataReadResult) {
        mDataReadResult = dataReadResult;
    }

    public DataReadResult getDataReadResult() {
        return mDataReadResult;
    }
}
