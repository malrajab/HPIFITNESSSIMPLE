package com.example.m_alrajab.hpi_simple_fitness.controller.event.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.m_alrajab.hpi_simple_fitness.R;
import com.example.m_alrajab.hpi_simple_fitness.model.UserFitModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by m_alrajab on 19.12.16.
 */
public class FitnessAdapter extends RecyclerView.Adapter<FitnessAdapter.ViewHolder> {
    private Context mContext;
    private List<UserFitModel> mUserFitModelList = new ArrayList<>();

    public FitnessAdapter(Context context) {
        mContext = context;
    }

    @Override
    public FitnessAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FitnessAdapter.ViewHolder holder, int position) {
        holder.bindView(mUserFitModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return mUserFitModelList.size();
    }

    public void setUserFitModelList(List<UserFitModel> modelList) {
        mUserFitModelList = modelList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_steps)
        TextView mStepsTextView;
        @BindView(R.id.tv_date_range)
        TextView mDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(UserFitModel userFitModel) {
            mStepsTextView.setText(userFitModel.getPeriodicStepCount());
            mDateTextView.setText(userFitModel.getDate());
        }
    }
}
