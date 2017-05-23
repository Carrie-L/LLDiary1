package com.carrie.lldiary.helper;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.carrie.lldiary.R;

/**
 * Created by new on 2017/4/14.
 * 倒计时
 */

public class TimeCountDown extends CountDownTimer{
    private Button mButton;
    private Activity activity;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCountDown(Activity activity, Button button, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mButton=button;
        this.activity=activity;
        mButton.setClickable(false);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mButton.setText(String.format(activity.getString(R.string.resend_code), millisUntilFinished / 1000+""));
    }

    @Override
    public void onFinish() {
        mButton.setClickable(true);
        mButton.setText(activity.getString(R.string.send_code));
    }
}
