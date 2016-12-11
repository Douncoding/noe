package com.douncoding.noe.ui.component;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.douncoding.noe.R;
import com.douncoding.noe.model.Beacon;

import java.util.List;

public class BeaconItemAdapter extends RecyclerView.Adapter<BeaconItemAdapter.ViewHolder> {
    private List<Beacon> dataSet;
    private OnCallback callback;
    public interface OnCallback {
        void onItemClicked(Beacon beacon);
    }

    public BeaconItemAdapter(List<Beacon> dataSet, OnCallback callback) {
        this.dataSet = dataSet;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_list_item_beacon, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Beacon item = dataSet.get(position);

        holder.uuid.setText(item.getUuid());
        holder.major.setText(item.getMajor());
        holder.minor.setText(item.getMinor());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView uuid, major, minor;

        public ViewHolder(View itemView) {
            super(itemView);
            uuid = (TextView)itemView.findViewById(R.id.uuid);
            major = (TextView)itemView.findViewById(R.id.major);
            minor = (TextView)itemView.findViewById(R.id.minor);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (callback != null) {
                Beacon item = dataSet.get(getAdapterPosition());
                callback.onItemClicked(item);
            }
        }
    }
}
