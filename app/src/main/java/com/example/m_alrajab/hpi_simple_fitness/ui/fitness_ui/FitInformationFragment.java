package com.example.m_alrajab.hpi_simple_fitness.ui.fitness_ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.m_alrajab.hpi_simple_fitness.R;
import com.example.m_alrajab.hpi_simple_fitness.ui.InformationActivity;
import com.example.m_alrajab.hpi_simple_fitness.controller.event.adapter.FitnessAdapter;
import com.example.m_alrajab.hpi_simple_fitness.model.UserFitModel;
import com.example.m_alrajab.hpi_simple_fitness.controller.event.presenter.fit_presenter.FitInfoPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FitInformationFragment extends Fragment implements FitInformationView {
    private static final String TAG = FitInformationFragment.class.getSimpleName();
    private FitnessAdapter mFitnessAdapter;
    private FitInfoPresenter mFitInfoPresenter;
    private OnFitInfoFragmentInteractionListener mListener;

    @BindView(R.id.rv_fit_history)
    RecyclerView mFitRecyclerView;
    @BindView(R.id.tv_total_steps)
    TextView mTotalStepsTextView;

    public interface OnFitInfoFragmentInteractionListener {
        void onDailyActivityClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fit_information, container, false);
        ButterKnife.bind(FitInformationFragment.this, view);
        mFitInfoPresenter = new FitInfoPresenter(getContext(), FitInformationFragment.this);
        mFitInfoPresenter.downloadInfo();
        initRecyclerView();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (InformationActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFitInfoFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mFitInfoPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFitInfoPresenter.onStop();
    }

    @Override
    public void showStepsData(List<UserFitModel> modelList) {
        mFitnessAdapter.setUserFitModelList(modelList);
    }

    @Override
    public void showTotalSteps(String steps) {
        mTotalStepsTextView.setText(steps);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_daily_activity:
                mListener.onDailyActivityClick();
                break;
            case R.id.menu_register_sensor:
                mFitInfoPresenter.initStepCounter();
                break;
            case R.id.menu_unregister_sensor:
                mFitInfoPresenter.unregisterFitnessDataListener();
                mTotalStepsTextView.setText("");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        mFitRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFitnessAdapter = new FitnessAdapter(getContext());
        mFitRecyclerView.setAdapter(mFitnessAdapter);
    }
}
