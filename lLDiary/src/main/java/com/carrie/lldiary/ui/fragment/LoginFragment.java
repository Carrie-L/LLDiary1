package com.carrie.lldiary.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.HomeActivity;
import com.carrie.lldiary.entity.MyUser;
import com.carrie.lldiary.ui.SignActivity;
import com.carrie.lldiary.utils.AlertDialogUtils;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.EditTextView;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by new on 2017/4/14.
 */

public class LoginFragment extends BaseFragment {

    private static final String TAG = "LoginFragment";
    private EditTextView etvAccount;
    private EditTextView etvPwd;
    private Button btnPost;
    private TextView tvForgetPwd;
    private View view;
    BmobFile bmobFile;

    @Override
    View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.layout_login, container, false);
        return view;
    }

    @Override
    void initData(Bundle savedInstanceState) {
        findView();
        initEditText();
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });
    }

    private void login() {
        if (TextUtils.isEmpty(etvAccount.getEditText())) {
            AppUtils.toast0(getActivity(), getString(R.string.toast_phone_null));
            etvAccount.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etvPwd.getEditText())) {
            AppUtils.toast0(getActivity(), getString(R.string.toast_userpwd_null));
            etvPwd.requestFocus();
            return;
        }

        post();
    }

    private void post() {
        BmobUser.loginByAccount(getActivity(), etvAccount.getEditText(), etvPwd.getEditText(), new LogInListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException arg1) {
                if (user != null) {
                    AppUtils.toast0(getActivity(), getString(R.string.toast_login_success));
                    App.mCurrUser = BmobUser.getCurrentUser(getActivity());
                    App.mSP_account.edit().putString(Constant.account_phone, etvAccount.getEditText()).putString(Constant.account_userpwd, etvPwd.getEditText()).apply();
                    downAvatar();
                    enterHome();
                } else {
                    LogUtil.e(TAG, "BmobException: arg1.getErrorCode()=" + arg1.getErrorCode() + "||| arg1=" + arg1 + "，user=" + user);
                    AppUtils.bmobFailToast(getActivity(), arg1.getErrorCode());
                }
            }
        });
    }

    /**
     * 下载头像
     */
    private void downAvatar() {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addQueryKeys("avatar");
        query.getObject(getActivity(), App.mCurrUser.getObjectId(), new GetListener<MyUser>() {

            @Override
            public void onSuccess(MyUser arg0) {
                LogUtil.i(TAG, "查询成功+arg0.toString=" + arg0.toString());
                bmobFile = arg0.avatar;

                if (bmobFile != null) {
                    bmobFile.download(getActivity(), new File(AppUtils.avatarPath(etvAccount.getEditText())),
                            new DownloadFileListener() {
                                @Override
                                public void onSuccess(String arg0) {
                                    AppUtils.toast0(getActivity(), "下载头像成功");
                                }

                                @Override
                                public void onFailure(int arg0, String arg1) {
                                    AppUtils.failedLog("下载头像失败", arg0, arg1);
                                }
                            });
                }
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                AppUtils.failedLog("查询失败", arg0, arg1);
            }
        });
    }

    private void forgetPassword() {
        AlertDialogUtils.itemsChooseDialog(getActivity(), getString(R.string.forget_item_title), new String[]{getString(R.string.forget_item_phone), getString(R.string.forget_item_email)},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.i(TAG, "which=" + which);
                        if (which == 0) {
                            ((SignActivity)getActivity()).resetTitle();
                            resetByPhone();
                        } else {
                            resetByEmail();
                        }
                    }
                });
    }

    private void resetByPhone() {
        LogUtil.i(TAG, "手机号重置");
        Bundle arg = new Bundle();
        arg.putBoolean("RESET", true);
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.setArguments(arg);
        getFragmentManager().beginTransaction().replace(R.id.frame_sign, registerFragment).commit();
    }

    private void resetByEmail() {
        LogUtil.i(TAG, "邮箱重置");
    }

    private void enterHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void initEditText() {
        etvAccount.setText(R.string.register_account);
        etvPwd.setText(R.string.register_password);

        etvAccount.setInputType(InputType.TYPE_CLASS_PHONE);
        etvPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        etvAccount.setFocusMode(true);
        etvPwd.setFocusMode(true);

        etvAccount.setSelection(etvAccount.getEditText().length());
        etvPwd.setSelection(etvPwd.getEditText().length());

        etvPwd.showClearButton(true);
        etvAccount.showClearButton(true);
    }

    private void findView() {
        etvAccount = (EditTextView) view.findViewById(R.id.met_account);
        etvPwd = (EditTextView) view.findViewById(R.id.met_password);
        btnPost = (Button) view.findViewById(R.id.btn_post);
        tvForgetPwd = (TextView) view.findViewById(R.id.tv_forget_pwd);
        if(!TextUtils.isEmpty(AppUtils.getPhoneNumber())){
            etvAccount.setEditText(AppUtils.getPhoneNumber());

            etvPwd.requestFocus();
        }
    }
}
