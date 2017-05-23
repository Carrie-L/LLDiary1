package com.carrie.lldiary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.HomeActivity;
import com.carrie.lldiary.base.BaseHandler;
import com.carrie.lldiary.helper.TimeCountDown;
import com.carrie.lldiary.ui.fragment.LoginFragment;
import com.carrie.lldiary.ui.fragment.RegisterFragment;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.utils.PermissionUtils;

import cn.smssdk.SMSSDK;

/**
 * Created by new on 2017/4/13.
 * 登录/注册
 */

public class SignActivity extends AppCompatActivity {
    private final String TAG = "SignActivity";
    private static final int CHANGE_TITLE = 1;
    private RegisterFragment mRegisterFragment;
    private TextView btnExchange;
    private TextView tvTitle;
    private boolean isNowRegister;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //取消状态栏
        setContentView(R.layout.activity_sign);

        btnExchange = (TextView) findViewById(R.id.btn_exchange_sign);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        RelativeLayout llSign = (RelativeLayout) findViewById(R.id.ll_sign);
        llSign.getBackground().setAlpha(127);//设置不透明度,0-255

        boolean isFirstStartApp = AppUtils.isFirstStartApp();
        LogUtil.i(TAG, "isFirstStartApp=" + isFirstStartApp);

        if (isFirstStartApp) {
            App.mSP_config.edit().putBoolean("isFirstStartApp", false).apply();
            transRegister();
        } else {
            if (App.mCurrUser != null) {//自动登录
                enterHome();
            }else{
                exchangeSign(!TextUtils.isEmpty(AppUtils.getPhoneNumber()));
            }
        }

        btnExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangeSign(isNowRegister);
            }
        });
    }

    /**
     * 交换注册/登录
     *
     * @param bool true：Login；false:Register
     */
    private void exchangeSign(boolean bool) {
        if (bool) {
            transLogin();
        } else {
            transRegister();
        }
    }

    private void transRegister() {
        isNowRegister = true;
        tvTitle.setText(getString(R.string.register));
        btnExchange.setText(getString(R.string.login));
        mRegisterFragment = new RegisterFragment();
        getFragmentManager().beginTransaction().replace(R.id.frame_sign, mRegisterFragment).commit();
    }

    private void transLogin() {
        isNowRegister = false;
        tvTitle.setText(getString(R.string.login));
        btnExchange.setText(getString(R.string.register));
        getFragmentManager().beginTransaction().replace(R.id.frame_sign, new LoginFragment()).commit();
    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        if (App.mCurrUser != null) {
            enterHome();
        }
    }

    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void resetTitle() {
        mHandler.sendEmptyMessage(CHANGE_TITLE);
    }

    RegisterHandler mHandler = new RegisterHandler(this);

    private static class RegisterHandler extends BaseHandler<SignActivity> {
        private RegisterHandler(SignActivity activity) {
            super(activity);
        }

        @Override
        public void handleMsg(Message msg, SignActivity signActivity) {
            switch (msg.what) {
                case CHANGE_TITLE:
                    signActivity.tvTitle.setText(signActivity.getString(R.string.reset_pwd_title));
                    break;
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LogUtil.i(TAG, "requestCode=" + requestCode + ",permissions=" + permissions.length + ",grantResults=" + grantResults.length);
        if (requestCode == PermissionUtils.PMS_CODE_SEND_SMS) {
            if (PermissionUtils.getGrantResults(grantResults)) {
                LogUtil.i(TAG, "onRequestPermissionsResult：已经获取发送短信权限:" + AppUtils.getPhoneNumber());
                SMSSDK.getVerificationCode("86", AppUtils.getPhoneNumber());
                //60s倒计时
                TimeCountDown timeCountDown = new TimeCountDown(this, mRegisterFragment.btnSendCode, 60000, 1000);
                timeCountDown.start();
            } else {
                Toast.makeText(this, getString(R.string.toast_not_permission_sms), Toast.LENGTH_LONG).show();
            }
        }
    }

}
