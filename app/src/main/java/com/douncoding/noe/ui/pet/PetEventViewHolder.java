package com.douncoding.noe.ui.pet;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.douncoding.noe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by douncoding on 2016. 12. 6..
 */

public class PetEventViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.face_picture) ImageView faceview;
    @BindView(R.id.event_title) TextView title;
    @BindView(R.id.event_target) TextView description;
    @BindView(R.id.event_map) ViewGroup mapview;
    @BindView(R.id.event_time) TextView time;

    public PetEventViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
