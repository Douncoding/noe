package com.douncoding.noe.ui.pay_action.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.douncoding.noe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.bank_name) TextView bankname;
    @BindView(R.id.account_number) TextView account;
    public PayViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
