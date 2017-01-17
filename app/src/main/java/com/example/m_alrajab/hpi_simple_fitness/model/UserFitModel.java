package com.example.m_alrajab.hpi_simple_fitness.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by m_alrajab on 19.12.16.
 */

public class UserFitModel extends RealmObject {
    private String mPeriodicStepCount;

    @PrimaryKey
    private String mDate;

    public void setPeriodicStepCount(String periodicStepCount) {
        mPeriodicStepCount = periodicStepCount;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getPeriodicStepCount() {
        return mPeriodicStepCount;
    }
}
