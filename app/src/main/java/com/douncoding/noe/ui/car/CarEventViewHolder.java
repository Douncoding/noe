package com.douncoding.noe.ui.car;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.douncoding.noe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarEventViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.face_picture) ImageView faceview;
    @BindView(R.id.event_title) TextView title;
    @BindView(R.id.event_target) TextView description;
    @BindView(R.id.event_map) ViewGroup mapview;
    @BindView(R.id.event_time) TextView time;
    public CarEventViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
