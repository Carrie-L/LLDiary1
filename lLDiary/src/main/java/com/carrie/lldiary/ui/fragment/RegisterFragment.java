package com.carrie.lldiary.ui.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.HomeActivity;
import com.carrie.lldiary.helper.BmobHelper;
import com.carrie.lldiary.helper.TimeCountDown;
import com.carrie.lldiary.utils.AlertDialogUtils;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.utils.PermissionUtils;
import com.carrie.lldiary.view.EditTextView;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by new on 2017/4/13.
 * 注册页面(重置密码页面)
 */

public class RegisterFragment extends Fragment {
    private static final int VERIFY_SUCCESS = 1;//验证成功
    private static final int VERIFY_FAIL = -1;//验证失败
    private static final int AUTO_VERIFY_SUCCESS = 0;//智能验证成功
    private final String TAG = "RegisterFragment";
    private View view;
    private EditTextView etvAccount;
    private EditTextView etvCode;
    private EditTextView etvPwd;
    private EditTextView etvConfirmPwd;
    public Button btnSendCode;
    private Button btnPost;
    private boolean autoVerifySuccess;
    private EventHandler mSMSEventHandler;
    private boolean mResetPwd;
    private TextView tvSkip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.layout_register, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mResetPwd = args.getBoolean("RESET", false);
            LogUtil.i(TAG, "mResetPwd=" + mResetPwd);
        }

        findView();
        initEditText();
        registerSMSEventHandler();

        //发送验证码
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSendCode();
            }
        });
        //提交
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterHome();
            }
        });
    }

    private void clickSendCode() {
        if (!TextUtils.isEmpty(etvAccount.getEditText())) {
            App.mSP_account.edit().putString(Constant.account_phone, etvAccount.getEditText()).apply();
            if (Build.VERSION.SDK_INT >= 23) {
                requestSMSPermission();
            } else {
                sendCode();
            }
        }
    }

    /**
     * 提交注册
     */
    private void post() {
        btnPost.setClickable(false);
        BmobUser user = new BmobUser();
        user.setUsername(DateUtil.getCurrentTimeLong() + "");//如果UserName为空会注册失败，且唯一；注册时不填写用户名，因此默认设置为当前时间
        user.setMobilePhoneNumber(etvAccount.getEditText());
        user.setMobilePhoneNumberVerified(true);
        user.setPassword(etvPwd.getEditText());

        user.signUp(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                AppUtils.toast1(getActivity(), getString(R.string.toast_register_success));
                App.mCurrUser = BmobUser.getCurrentUser(getActivity());
                App.mSP_account.edit().putString(Constant.account_phone, etvAccount.getEditText()).putString(Constant.account_userpwd, etvPwd.getEditText()).apply();
                enterHome();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                LogUtil.e(TAG, "注册失败: arg0=" + arg0 + "，arg1=" + arg1);
                App.mSP_account.edit().putString(Constant.account_phone, "").apply();
//                if (arg1.contains("email") && arg1.contains("already taken")) {// 邮箱已存在
//                    AppUtils.toast1(getActivity(), getString(R.string.toast_register_failed_email));
//                } else if (arg1.contains("username") && arg1.contains("already taken")) {// 用户名已存在
//                    AppUtils.toast1(getActivity(), getString(R.string.toast_register_failed_username));
//                    etvAccount.requestFocus();
//                } else
                if (arg0 == 9010) {// 网络超时
                    AppUtils.toast1(getActivity(), getString(R.string.toast_network_outtime));
                } else if (arg0 == 9016) {// 无网络连接，请检查您的手机网络。
                    AppUtils.toast1(getActivity(), getString(R.string.toast_network_error));
                } else if (arg0 == 304) {
                    AppUtils.toast1(getActivity(), getString(R.string.toast_register_failed_username_pwd_null));
                } else if (arg0 == 209) {
                    AppUtils.toast1(getActivity(), getString(R.string.toast_register_failed_phone_exists));
                    App.mSP_account.edit().putString(Constant.account_phone, etvAccount.getEditText()).apply();//手机号已存在，因此保存手机号，在【登录】时直接显示
                }
                btnPost.setClickable(true);

            }
        });
    }

    public void register() {
        if (TextUtils.isEmpty(etvAccount.getEditText())) {
            AppUtils.toast0(getActivity(), getString(R.string.toast_phone_null));
            etvAccount.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etvCode.getEditText())) {
            AppUtils.toast0(getActivity(), getString(R.string.toast_code_null));
            etvCode.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etvPwd.getEditText())) {
            AppUtils.toast0(getActivity(), getString(R.string.toast_userpwd_null));
            etvPwd.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etvConfirmPwd.getEditText())) {
            AppUtils.toast0(getActivity(), getString(R.string.toast_confirm_pwd_null));
            etvConfirmPwd.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(etvConfirmPwd.getEditText()) && !etvPwd.getEditText().equals(etvConfirmPwd.getEditText())) {
            AppUtils.toast0(getActivity(), getString(R.string.toast_password_not_same));
            etvConfirmPwd.requestFocus();
            return;
        }

        if (mResetPwd) {//重置密码
            resetPassword();
        } else {//注册
            if (autoVerifySuccess) {
                LogUtil.i(TAG, "已经智能验证通过，直接提交");
                post();
            } else//第一次验证，验证验证码，验证码通过后提交
                SMSSDK.submitVerificationCode("86", etvAccount.getEditText(), etvCode.getEditText());
        }
    }

    private void sendCode() {
        if (mResetPwd) {
            resetFindUserThenSendCode();
        } else {
            sendSMSCode();
        }
    }

    /**
     * 发送验证码
     */
    private void sendSMSCode() {
        SMSSDK.getVerificationCode("86", etvAccount.getEditText());
        //60s倒计时
        TimeCountDown timeCountDown = new TimeCountDown(getActivity(), btnSendCode, 60000, 1000);
        timeCountDown.start();
    }

    private void requestSMSPermission() {
        if (getSMSPermission()) {
            LogUtil.i(TAG, "已经获取发送短信权限");
            sendCode();
        } else {
            LogUtil.i(TAG, "请求发送短信权限");
            AlertDialogUtils.showOKDialog(getActivity(), getString(R.string.dialog_sms_title), getString(R.string.dialog_sms_message), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PermissionUtils.requestPermission(getActivity(), PermissionUtils.PERMISSION_RECEIVE_SMS, PermissionUtils.PMS_CODE_SEND_SMS);
                }
            });
        }
    }

    /**
     * 是否获取到短信权限
     *
     * @return bool
     */
    private boolean getSMSPermission() {
        return PermissionUtils.checkPermission(getActivity(), PermissionUtils.PERMISSION_RECEIVE_SMS);
    }

    private void resetFindUserThenSendCode() {
        LogUtil.i(TAG, "onSendMessage_mResetPwd");
        BmobHelper.findUser(getActivity(), BmobHelper.COLUMN_PHONE_NUMBER, etvAccount.getEditText(), new FindListener<BmobUser>() {
            @Override
            public void onSuccess(List<BmobUser> list) {
                //如果User表里有该手机号：注册——提示已有，请登录；重置：可以发送验证码。
                //如果没有：注册——可以发送验证码；重置——提示不存在，请注册
                if (list.size() > 0) {
                    if (mResetPwd) {
                        sendSMSCode();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.phone_exists_login), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (mResetPwd) {
                        Toast.makeText(getActivity(), getString(R.string.phone_not_exists_register), Toast.LENGTH_LONG).show();
                    } else {
                        sendSMSCode();
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e(TAG, "i=" + i + ",s=" + s);
            }
        });
    }

    private void resetPassword() {
        BmobUser bmobUser = new BmobUser();
        bmobUser.setPassword(etvPwd.getEditText());
        BmobHelper.updateUser(getActivity(), bmobUser, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), getString(R.string.reset_pwd_success), Toast.LENGTH_LONG).show();
                enterHome();
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.e(TAG, "updateUser：i=" + i + ",s=" + s);
            }
        });
    }

    TextWatcher mPwdTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s.toString())) {
                etvPwd.showClearButton(true);
                if (!TextUtils.isEmpty(etvConfirmPwd.getEditText()) && s.toString().length() == etvConfirmPwd.getEditText().length() && !s.toString().equals(etvConfirmPwd.getEditText())) {
                    Toast.makeText(getActivity(), getString(R.string.toast_password_not_same), Toast.LENGTH_LONG).show();
                }
            } else {
                etvPwd.showClearButton(true);
            }
        }
    };

    TextWatcher mConfirmPwdTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (!TextUtils.isEmpty(etvPwd.getEditText())) {
                etvConfirmPwd.setEditTextLength(etvPwd.getEditText().length());
                etvConfirmPwd.setMaxCharacters(etvPwd.getEditText().length());
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(etvPwd.getEditText()) && !TextUtils.isEmpty(s.toString()) && s.toString().length() == etvPwd.getEditText().length() && !s.toString().trim().equals(etvPwd.getEditText())) {
                Toast.makeText(getActivity(), getString(R.string.toast_password_not_same), Toast.LENGTH_LONG).show();
                etvConfirmPwd.showClearButton(true);
            } else if (TextUtils.isEmpty(s.toString())) {
                etvConfirmPwd.showClearButton(false);
            }
        }
    };

    private RegisterHandler mHandler = new RegisterHandler(this);

    private static class RegisterHandler extends Handler {
        private WeakReference<RegisterFragment> reference;

        private RegisterHandler(RegisterFragment activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RegisterFragment fragment = reference.get();
            switch (msg.what) {
                case VERIFY_SUCCESS:
                    LogUtil.i(fragment.TAG, "验证成功");
                    fragment.post();
                    break;
                case AUTO_VERIFY_SUCCESS:
                    fragment.etvCode.setEditText("1111");
                    Toast.makeText(fragment.getActivity(), "智能验证短信成功", Toast.LENGTH_LONG).show();
                    fragment.autoVerifySuccess = true;
                    break;
                case VERIFY_FAIL:
                    LogUtil.i(fragment.TAG, "验证失败");
                    Toast.makeText(fragment.getActivity(), "短信验证失败！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private void registerSMSEventHandler() {
        mSMSEventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                LogUtil.i(TAG, "data=" + data.toString() + ", result=" + result);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    switch (event) {
                        case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE://提交验证码成功
                            //验证成功，则提交注册
                            mHandler.sendEmptyMessage(VERIFY_SUCCESS);
                            break;
                        case SMSSDK.EVENT_GET_VERIFICATION_CODE://发送验证码成功
                            LogUtil.i(TAG, "获取验证码成功：" + data.toString());
                            if (data.toString().equals("true")) {
                                //智能短信验证成功
                                mHandler.sendEmptyMessage(AUTO_VERIFY_SUCCESS);
                            }
                            break;
                    }
                } else {
                    LogUtil.e(TAG, "短信错误");
                    mHandler.sendEmptyMessage(VERIFY_FAIL);
                }
            }
        };
        SMSSDK.registerEventHandler(mSMSEventHandler); //注册短信回调
    }

    private void enterHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(mSMSEventHandler);
    }

    private void initEditText() {
        etvAccount.setText(R.string.register_account);
        etvCode.setText(R.string.register_code);
        etvPwd.setText(R.string.register_password);
        etvConfirmPwd.setText(R.string.register_confirm_password);

        etvAccount.setInputType(InputType.TYPE_CLASS_PHONE);
        etvCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        etvPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etvConfirmPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        etvAccount.setFocusMode(true);
        etvCode.setFocusMode(true);
        etvPwd.setFocusMode(true);
        etvConfirmPwd.setFocusMode(true);

        etvAccount.setSelection(etvAccount.getEditText().length());
        etvCode.setSelection(etvCode.getEditText().length());
        etvPwd.setSelection(etvPwd.getEditText().length());
        etvConfirmPwd.setSelection(etvConfirmPwd.getEditText().length());

        etvPwd.addTextChangeListener(mPwdTextWatcher);
        etvConfirmPwd.addTextChangeListener(mConfirmPwdTextWatcher);
    }

    private void findView() {
        etvAccount = (EditTextView) view.findViewById(R.id.met_account);
        etvCode = (EditTextView) view.findViewById(R.id.met_code);
        etvPwd = (EditTextView) view.findViewById(R.id.met_password);
        etvConfirmPwd = (EditTextView) view.findViewById(R.id.met_confirm_password);
        btnSendCode = (Button) view.findViewById(R.id.btn_send_code);
        btnPost = (Button) view.findViewById(R.id.btn_post);
        tvSkip = (TextView) view.findViewById(R.id.register_skip);
        if (mResetPwd) {
            tvSkip.setVisibility(View.GONE);
        }
    }

}
