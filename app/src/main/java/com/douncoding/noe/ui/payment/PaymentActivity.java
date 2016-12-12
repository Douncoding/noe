package com.douncoding.noe.ui.payment;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.douncoding.noe.R;
import com.douncoding.noe.model.Payment;
import com.douncoding.noe.util.FirebaseUtil;
import com.dpizarro.pinview.library.PinView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class PaymentActivity extends AppCompatActivity {
    public static void startActivity(Context context, String paymentKey) {
        if (context != null) {
            Intent callingIntent = new Intent(context, PaymentActivity.class);
            callingIntent.putExtra("EXTRA.PAYMENT_KEY", paymentKey);
            callingIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(callingIntent);
        }
    }

    String paymentKey;

    @BindView(R.id.text1) TextView mProductName;
    @BindView(R.id.text2) TextView mProductPrice;
    @BindView(R.id.pinView) PinView mPinView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);

        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setDisplayShowTitleEnabled(false);

        paymentKey = getIntent().getStringExtra("EXTRA.PAYMENT_KEY");
        FirebaseUtil.getPaymentRef().child(paymentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Logger.i("확인" + dataSnapshot.getValue().toString());
                    Payment payment = dataSnapshot.getValue(Payment.class);
                    setupPaymentView(payment);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mPinView.getWindowToken(), 0);
    }

    private int tryCount = 0;
    private void setupPaymentView(Payment payment) {
        mProductName.setText(payment.getProduct());
        mProductPrice.setText(payment.getPrice());

        mPinView.setOnCompleteListener(new PinView.OnCompleteListener() {
            @Override
            public void onComplete(boolean completed, String pinResults) {
                if (completed ) {
                    if (pinResults.equals(payment.getPassword())) {
                        Toast.makeText(PaymentActivity.this, "결제요청을 수락하였습니다.", Toast.LENGTH_SHORT).show();
                        handleRequestPayment(payment);
                        finish();
                    } else {
                        if (tryCount++ > 3) {
                            Toast.makeText(PaymentActivity.this, "3회 비밀번호가 틀렸습니다. 결제요청을 취소합니다.", Toast.LENGTH_SHORT).show();
                            mPinView.clear();
                            finish();
                        }
                    }
                }
            }
        });
    }

    /**
     * 사용자가 패스워드를 정상입력하여 결제를 수락하는 케이스를 담당
     */
    private void handleRequestPayment(Payment payment) {
        // 상태랄 변경하여 요청한 단말쪽에서 확인할 수 있도록 한다.
        FirebaseUtil.getPaymentRef().child(paymentKey).child("status").setValue(Payment.Status.ING);
    }
}
