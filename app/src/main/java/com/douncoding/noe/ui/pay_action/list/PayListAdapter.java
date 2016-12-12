package com.douncoding.noe.ui.pay_action.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.douncoding.noe.R;
import com.douncoding.noe.model.Pay;

import java.util.ArrayList;
import java.util.List;


public class PayListAdapter extends RecyclerView.Adapter<PayViewHolder> {
    private List<Pay> dataSet;

    public PayListAdapter(List<Pay> items) {
        dataSet = items;
    }

    @Override
    public PayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_pay, parent, false);
        return new PayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PayViewHolder holder, int position) {
        Pay pay = dataSet.get(position);

        holder.bankname.setText(pay.getBankname());
        holder.account.setText(pay.getAccount());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
