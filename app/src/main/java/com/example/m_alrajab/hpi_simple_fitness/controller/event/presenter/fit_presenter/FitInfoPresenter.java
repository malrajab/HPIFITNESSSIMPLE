package com.example.m_alrajab.hpi_simple_fitness.controller.event.presenter.fit_presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.example.m_alrajab.hpi_simple_fitness.Application;
import com.example.m_alrajab.hpi_simple_fitness.controller.event.event.DataLoadFinishedEvent;
import com.example.m_alrajab.hpi_simple_fitness.controller.event.event.DataSavedEvent;
import com.example.m_alrajab.hpi_simple_fitness.controller.event.event.StepsUpdatedEvent;
import com.example.m_alrajab.hpi_simple_fitness.jobs.FetchUserFitnessActivityJob;
import com.example.m_alrajab.hpi_simple_fitness.jobs.ParseSaveJob;
import com.example.m_alrajab.hpi_simple_fitness.model.UserFitModel;
import com.example.m_alrajab.hpi_simple_fitness.ui.fitness_ui.FitInformationView;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;

import static com.example.m_alrajab.hpi_simple_fitness.Application.sGoogleApiClient;


/**
 * Created by m_alrajab on 20.12.16.
 */

public class FitInfoPresenter implements IFitInfoPresenter {
    private static final String TAG = FitInfoPresenter.class.getSimpleName();
    private JobManager mJobManager;
    private Context mContext;
    private FitInformationView mFitInformationView;
    private OnDataPointListener mListener;

//    =================================== LISTENERS AREA

    private ResultCallback<? super Status> mResultCallback = new ResultCallback<Status>() {
        @Override
        public void onResult(@NonNull Status status) {
            if (status.isSuccess()) {
                if (status.getStatusCode()
                        == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                    Log.i(TAG, "Existing subscription for activity detected.");
                } else {
                    Log.i(TAG, "Successfully subscribed!");
                }
            } else {
                Log.w(TAG, "There was a problem subscribing." + status);
            }
        }
    };

    private ResultCallback<DataSourcesResult> mSensorApiResultCallback = new ResultCallback<DataSourcesResult>() {
        @Override
        public void onResult(DataSourcesResult dataSourcesResult) {
            Log.i(TAG, "Result: " + dataSourcesResult.getStatus().toString());
            for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                Log.i(TAG, "Data source found: " + dataSource.toString());
                Log.i(TAG, "Data Source type: " + dataSource.getDataType().getName());

                //Let's register a listener to receive Activity data!
                if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                        && mListener == null) {
                    Log.i(TAG, "Data source for TYPE_STEP_COUNT_CUMULATIVE found!  Registering.");
                    registerFitnessDataListener(dataSource,
                            DataType.TYPE_STEP_COUNT_CUMULATIVE);
                }
            }
        }
    };

//    =================================== LISTENERS AREA END


    public FitInfoPresenter(Context context, FitInformationView view) {
        mContext = context;
        mJobManager = Application.getInstance().getJobManager();
        mFitInformationView = view;
    }

    @Override
    public void downloadInfo() {
        Log.d(TAG, "downloadInfo: ");
        mJobManager.addJobInBackground(new FetchUserFitnessActivityJob());
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(FitInfoPresenter.this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(FitInfoPresenter.this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dataLoadFinishedEvent(DataLoadFinishedEvent event) {
        Log.d(TAG, "dataLoadFinishedEvent: ");
        mJobManager.addJobInBackground(new ParseSaveJob(event.getDataReadResult()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dateSavedEvent(DataSavedEvent event) {
        Log.d(TAG, "dateSavedEvent: ");
        queryDB();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void stepUpdate(StepsUpdatedEvent event) {
        mFitInformationView.showTotalSteps(event.getStepCount());
    }

    public void unregisterFitnessDataListener() {
        if (mListener == null) {
            return;
        }
        Fitness.SensorsApi.remove(
                sGoogleApiClient,
                mListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Listener was removed!");
                        } else {
                            Log.i(TAG, "Listener was not removed.");
                        }
                    }
                });
    }

    @Override
    public void initStepCounter() {
        subscribe();
        getSensorData();
    }

    private void subscribe() {
        Fitness.RecordingApi.subscribe(sGoogleApiClient, DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setResultCallback(mResultCallback);
    }

    private void queryDB() {
        Realm realm = Realm.getDefaultInstance();
        List<UserFitModel> models = realm.where(UserFitModel.class).findAll();
        mFitInformationView.showStepsData(models);
    }

    private void getSensorData() {
        Fitness.SensorsApi.findDataSources(sGoogleApiClient, new DataSourcesRequest.Builder()
                // At least one datatype must be specified.
                .setDataTypes(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setDataSourceTypes(DataSource.TYPE_RAW)
                .build())
                .setResultCallback(mSensorApiResultCallback);
    }

    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {
        mListener = new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                for (Field field : dataPoint.getDataType().getFields()) {
                    Value val = dataPoint.getValue(field);
                    Log.i(TAG, "Detected DataPoint field: " + field.getName());
                    Log.i(TAG, "Detected DataPoint value: " + val);
                    String stepValue = val.toString();
                    //non ui thread, so we need event bus or smth else
                    EventBus.getDefault().post(new StepsUpdatedEvent(stepValue));
                }
            }
        };
        Fitness.SensorsApi.add(
                sGoogleApiClient,
                new SensorRequest.Builder()
                        .setDataSource(dataSource) // Optional but recommended for custom data sets.
                        .setDataType(dataType) // Can't be omitted.
                        .setSamplingRate(10, TimeUnit.SECONDS)
                        .build(),
                mListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Listener registered!");
                        } else {
                            Log.i(TAG, "Listener not registered.");
                        }
                    }
                });
    }
}
