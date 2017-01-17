package com.example.m_alrajab.hpi_simple_fitness.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.m_alrajab.hpi_simple_fitness.R;
import com.example.m_alrajab.hpi_simple_fitness.ui.today_fragments.ShowDailyInformationFragment;
import com.example.m_alrajab.hpi_simple_fitness.ui.fitness_ui.FitInformationFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import io.realm.Realm;

import static com.example.m_alrajab.hpi_simple_fitness.Application.sGoogleApiClient;

/**
 * Created by m_alrajab on 19.12.16.
 */

public class InformationActivity extends AppCompatActivity implements FitInformationFragment.OnFitInfoFragmentInteractionListener {
    private static final int RC_SIGN_IN = 2;
    private static final String TAG = InformationActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sGoogleApiClient != null)
            sGoogleApiClient.disconnect();
    }

    @Override
    public void onDailyActivityClick() {
        replaceFragment(new ShowDailyInformationFragment(), true);
    }

    private void init() {
        sGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
        Realm.init(InformationActivity.this);
        signIn();
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment);

        if (addToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(sGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            replaceFragment(new FitInformationFragment(), false);
        } else {
            Log.d(TAG, "handleSignInResult:Result bad ");
            // Signed out, show unauthenticated UI.
        }
    }
}
