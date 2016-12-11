package com.douncoding.noe.ui.pets_action.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.douncoding.noe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.pet_picture) ImageView faceview;
    @BindView(R.id.pet_name) TextView petname;
    @BindView(R.id.pet_nickname) TextView nickname;
    @BindView(R.id.pet_birthday) TextView birthday;
    @BindView(R.id.pet_beacon) TextView beacon;
    @BindView(R.id.pet_start_action) TextView startAction;
    @BindView(R.id.pet_stop_action) TextView stopAction;
    @BindView(R.id.pet_track_state) ViewGroup trackstate;

    public PetViewHolder(View itemView) {
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
