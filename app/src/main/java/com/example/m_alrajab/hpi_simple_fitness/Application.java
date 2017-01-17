package com.example.m_alrajab.hpi_simple_fitness;

import android.os.Bundle;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;


/**
 * Created by m_alrajab on 20.12.16.
 */

public class Application extends android.app.Application {
    public static GoogleApiClient sGoogleApiClient = null;
    private static final String TAG = Application.class.getSimpleName();
    private static JobManager mJobManager;
    private static Application mInstance;

    //    ==============================================LISTENER AREA
    private GoogleApiClient.ConnectionCallbacks mConnectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            Log.i(TAG, "Connected!!!");
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.d(TAG, "onConnectionSuspended: " + i);
            if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                Log.i(TAG, "Connection lost.  Cause: Network Lost.");
            } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
            }
        }
    };

    //    ==============================================LISTENER AREA END
    @Override
    public void onCreate() {
        super.onCreate();
        configureJobManager();
        buildFitnessClient();
    }

    private void buildFitnessClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .build();

        sGoogleApiClient = new GoogleApiClient.Builder(Application.this)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.SENSORS_API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(mConnectionCallbacks)
                .build();
    }


    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(6)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        if (mJobManager == null)
            mJobManager = new JobManager(configuration);
    }

    public JobManager getJobManager() {
        return mJobManager;
    }

    public static Application getInstance() {
        if (mInstance == null)
            mInstance = new Application();
        return mInstance;
    }
}
