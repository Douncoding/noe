package com.douncoding.noe.ui.car_action.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.douncoding.noe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.car_picture) ImageView faceview;
    @BindView(R.id.car_name) TextView carname;
    @BindView(R.id.car_licenseplate) TextView licenseplate;
    @BindView(R.id.car_birthday) TextView birthday;
    @BindView(R.id.car_beacon) TextView beacon;

    public CarViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(view -> {
            if (onListener != null)
                onListener.onItemClicked();
        });
    }

    private OnListener onListener;
    public interface OnListener {
        void onItemClicked();
    }

    public void setOnListener(OnListener listener) {
        this.onListener = listener;
    }
}
