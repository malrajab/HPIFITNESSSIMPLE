package com.example.m_alrajab.hpi_simple_fitness.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.m_alrajab.hpi_simple_fitness.controller.event.event.DataSavedEvent;
import com.example.m_alrajab.hpi_simple_fitness.model.UserFitModel;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DataReadResult;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;

import static java.text.DateFormat.getDateInstance;

/**
 * Created by m_alrajab on 20.12.16.
 */

public class ParseSaveJob extends Job {
    private static final String TAG = ParseSaveJob.class.getSimpleName();
    private static final int PRIORITY = 5;
    private DataReadResult mDataReadResult;

    public ParseSaveJob(DataReadResult dataReadResult) {
        super(new Params(PRIORITY));
        mDataReadResult = dataReadResult;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Log.d(TAG, "onRun: " + mDataReadResult);
        printData(mDataReadResult);
        EventBus.getDefault().post(new DataSavedEvent());
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }

    private void printData(DataReadResult dataReadResult) {
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
    }

    private void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getDateInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            String startDate = dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS));

            UserFitModel userFitModel = new UserFitModel();

            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.e("History", "\tDate: " + startDate);
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
                userFitModel.setDate(startDate);
                userFitModel.setPeriodicStepCount(dp.getValue(field).toString());
                saveToDB(userFitModel);
            }
        }
    }

    private void saveToDB(final UserFitModel model) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(model);
            }
        });
    }
}
