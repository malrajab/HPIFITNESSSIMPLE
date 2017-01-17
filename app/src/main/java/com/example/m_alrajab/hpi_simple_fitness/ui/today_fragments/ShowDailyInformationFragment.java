package com.example.m_alrajab.hpi_simple_fitness.ui.today_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.m_alrajab.hpi_simple_fitness.R;
import com.example.m_alrajab.hpi_simple_fitness.controller.event.presenter.daily_presenter.DailyInfoPresenter;
import com.google.android.gms.fitness.data.Field;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by m_alrajab on 21.12.16.
 */

public class ShowDailyInformationFragment extends Fragment implements ShowDailyInformationView {
    @BindView(R.id.tv_daily_steps)
    TextView mDailyStepsTextView;
    @BindView(R.id.tv_daily_calories)
    TextView mDailyCaloriesTextView;

    private DailyInfoPresenter mDailyInfoPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_info, container, false);
        ButterKnife.bind(ShowDailyInformationFragment.this, view);
        mDailyInfoPresenter = new DailyInfoPresenter(getContext(), ShowDailyInformationFragment.this);
        mDailyInfoPresenter.downloadDailyData();
        return view;
    }

    @Override
    public void showInfo(Map<String, String> dailyValueMap) {
        mDailyStepsTextView.setText(dailyValueMap.get(Field.FIELD_STEPS.toString()));
        mDailyCaloriesTextView.setText(dailyValueMap.get(Field.FIELD_CALORIES.toString()));
    }

    @Override
    public void onStart() {
        super.onStart();
        mDailyInfoPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mDailyInfoPresenter.onStop();
    }
}
