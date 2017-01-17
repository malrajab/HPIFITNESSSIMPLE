package com.example.m_alrajab.hpi_simple_fitness.ui.fitness_ui;

import com.example.m_alrajab.hpi_simple_fitness.model.UserFitModel;

import java.util.List;

/**
 * Created by m_alrajab on 20.12.16.
 */

public interface FitInformationView {
    void showStepsData(List<UserFitModel> modelList);
    void showTotalSteps(String steps);
}
