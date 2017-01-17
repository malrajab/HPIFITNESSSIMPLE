package com.example.m_alrajab.hpi_simple_fitness.controller.event.presenter.daily_presenter;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.example.m_alrajab.hpi_simple_fitness.Application;
import com.example.m_alrajab.hpi_simple_fitness.controller.event.event.DailyTotalDownloadFinishEvent;
import com.example.m_alrajab.hpi_simple_fitness.jobs.FetchDailyActivityJob;
import com.example.m_alrajab.hpi_simple_fitness.ui.today_fragments.ShowDailyInformationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by m_alrajab on 21.12.16.
 */

public class DailyInfoPresenter implements IDailyInfoPresenter {
    private static final String TAG = DailyInfoPresenter.class.getSimpleName();
    private ShowDailyInformationView mShowDailyInformationView;
    private JobManager mJobManager;
    private Context mContext;

    public DailyInfoPresenter(Context context, ShowDailyInformationView showDailyInformationView) {
        mShowDailyInformationView = showDailyInformationView;
        mContext = context;
        mJobManager = Application.getInstance().getJobManager();
    }

    @Override
    public void downloadDailyData() {
        mJobManager.addJobInBackground(new FetchDailyActivityJob());
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(DailyInfoPresenter.this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(DailyInfoPresenter.this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dailyActivityDownloaded(DailyTotalDownloadFinishEvent event) {
        mShowDailyInformationView.showInfo(event.getDailyValueMap());
    }
}
