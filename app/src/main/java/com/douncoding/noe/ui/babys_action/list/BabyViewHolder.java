package com.douncoding.noe.ui.babys_action.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.douncoding.noe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BabyViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.baby_picture) ImageView faceview;
    @BindView(R.id.baby_name) TextView petname;
    @BindView(R.id.baby_nickname) TextView nickname;
    @BindView(R.id.baby_birthday) TextView birthday;
    @BindView(R.id.baby_beacon) TextView beacon;
    @BindView(R.id.baby_start_action) TextView startAction;
    @BindView(R.id.baby_stop_action) TextView stopAction;
    @BindView(R.id.baby_track_state) ViewGroup trackstate;

    public BabyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        startAction.setOnClickListener(view -> {
            if (onListener != null)
                onListener.onStartActionClicked();
        });

        stopAction.setOnClickListener(view -> {
            if (onListener != null)
                onListener.onStopActionClicked();
        });

        itemView.setOnClickListener(view -> {
            if (onListener != null)
                onListener.onItemClicked();
        });
    }

    private OnListener onListener;
    public interface OnListener {
        void onItemClicked();
        void onStartActionClicked();
        void onStopActionClicked();
    }

    public void setOnListener(OnListener listener) {
        this.onListener = listener;
    }
}
