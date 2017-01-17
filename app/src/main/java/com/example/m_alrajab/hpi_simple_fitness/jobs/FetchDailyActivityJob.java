package com.example.m_alrajab.hpi_simple_fitness.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.m_alrajab.hpi_simple_fitness.controller.event.event.DailyTotalDownloadFinishEvent;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DailyTotalResult;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.m_alrajab.hpi_simple_fitness.Application.sGoogleApiClient;


/**
 * Created by m_alrajab on 21.12.16.
 */

public class FetchDailyActivityJob extends Job {
    private static final int PRIORITY = 5;
    private static final String TAG = FetchDailyActivityJob.class.getSimpleName();
    private Map<String, String> mDailyValueMap = new HashMap<>();

    public FetchDailyActivityJob() {
        super(new Params(PRIORITY));
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        getDailyInformation(DataType.TYPE_STEP_COUNT_DELTA, Field.FIELD_STEPS);
        getDailyInformation(DataType.TYPE_CALORIES_EXPENDED, Field.FIELD_CALORIES);
        EventBus.getDefault().post(new DailyTotalDownloadFinishEvent(mDailyValueMap));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }

    private void getDailyInformation(DataType dataType, Field whatToRead) {
        String total = null;

        PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(sGoogleApiClient, dataType);
        DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
        if (totalResult.getStatus().isSuccess()) {
            DataSet totalSet = totalResult.getTotal();
            total = totalSet.isEmpty()
                    ? ""
                    : totalSet.getDataPoints().get(0).getValue(whatToRead).toString();
        } else {
            Log.w(TAG, "There was a problem getting the daily info. ");
        }
        Log.i(TAG, "Total : " + total);

        mDailyValueMap.put(whatToRead.toString(), total);
    }
}
