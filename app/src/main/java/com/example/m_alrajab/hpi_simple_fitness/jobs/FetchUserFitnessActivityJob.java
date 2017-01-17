package com.example.m_alrajab.hpi_simple_fitness.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.m_alrajab.hpi_simple_fitness.controller.event.event.DataLoadFinishedEvent;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.example.m_alrajab.hpi_simple_fitness.Application.sGoogleApiClient;

/**
 * Created by m_alrajab on 20.12.16.
 */

public class FetchUserFitnessActivityJob extends Job {
    private static final int PRIORITY = 4;
    //    =============== TIME RANGE CONSTANTS
    private static final int DAY_HOUR_END = 23;
    private static final int DAY_MINUTE_END = 59;
    private static final int DAY_SECOND_END = 59;
    private static final int DAY_MILIS_END = 999;
    private static final int START_DAY = 0;

    private static final String TAG = FetchUserFitnessActivityJob.class.getSimpleName();

    public FetchUserFitnessActivityJob() {
        super(new Params(PRIORITY));
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        DataReadRequest readRequest = queryStepsData();
        DataReadResult dataReadResult =
                Fitness.HistoryApi.readData(sGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);
        EventBus.getDefault().post(new DataLoadFinishedEvent(dataReadResult));
    }


    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }

    private DataReadRequest queryStepsData() {
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);

        long endTime = getDayStartEnd(calendar, DAY_HOUR_END, DAY_MINUTE_END,
                DAY_SECOND_END, DAY_MILIS_END);

        calendar.add(Calendar.WEEK_OF_YEAR, -1);

        long startTime = getDayStartEnd(calendar, START_DAY, START_DAY,
                START_DAY, START_DAY);

        DateFormat dateFormat = DateFormat.getDateInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime) + " " + startTime);
        Log.i(TAG, "Range End: " + dateFormat.format(endTime) + " " + endTime);

        return new DataReadRequest.Builder()
                .aggregate(getAccurateDataSource(), DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
    }

    private long getDayStartEnd(Calendar calendar, int hour, int min, int sec, int milis) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        calendar.set(Calendar.MILLISECOND, milis);
        return calendar.getTimeInMillis();
    }

    @NonNull
    private DataSource getAccurateDataSource() {
        return new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();
    }
}
