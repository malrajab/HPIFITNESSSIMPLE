package com.example.m_alrajab.hpi_simple_fitness.controller.event.presenter.fit_presenter;

/**
 * Created by m_alrajab on 20.12.16.
 */
public interface IFitInfoPresenter {
    void downloadInfo();
    void onStart();
    void onStop();
    void initStepCounter();
}
