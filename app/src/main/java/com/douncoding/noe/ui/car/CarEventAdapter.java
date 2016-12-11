package com.douncoding.noe.ui.car;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.douncoding.noe.R;
import com.douncoding.noe.model.TrackEvent;
import com.douncoding.noe.ui.baby.BabyEventViewHolder;

import java.util.List;


public class CarEventAdapter extends RecyclerView.Adapter<CarEventViewHolder> {
    private List<TrackEvent> dataSet;
    private Context context;

    public CarEventAdapter(List<TrackEvent> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public CarEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_event, parent, false);
        this.context = parent.getContext();
        return new CarEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarEventViewHolder holder, int position) {
        TrackEvent event = dataSet.get(position);

        if (event.getFacePictureUrl() != null) {
            Glide.with(context)
                    .load(event.getFacePictureUrl())
                    .into(holder.faceview);
        }

        holder.description.setText(event.getDescription());
        holder.time.setText(DateUtils.getRelativeTimeSpanString(event.getTimestamp()));

        switch (event.getType()) {
            case BREAKAWAY:
                holder.title.setText("연결 끊김");
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.carPrimaryColor));
                break;
            case WARNNING:
                holder.title.setText("위험 상태변화");
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.carPrimaryColor));
                break;
            case NOTICE:
                holder.title.setText("주의 상태변화");
                break;
            case NORMAL:
                holder.title.setText("안전 상태변화");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
