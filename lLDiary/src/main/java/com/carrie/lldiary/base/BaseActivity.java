package com.carrie.lldiary.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.carrie.lldiary.R;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.TitleView;

/**
 * Created by new on 2017/4/14.
 * base
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected  String TAG="BaseActivity";
    protected int mLayoutId;
    protected FrameLayout frameLayout;
    protected int mRightIcon;
//    protected View.OnClickListener mRightClickListener;
    protected String mTitle;
    protected TitleView titleView;
    protected boolean mShowTitleView=true;
    protected boolean isPreservable=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        frameLayout = (FrameLayout) findViewById(R.id.frame_container);
        titleView = (TitleView) findViewById(R.id.title_view);
        initPre();
        setTitleView();
        getLayoutInflater().inflate(mLayoutId,frameLayout);
        initData();
        titleView.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
    }

    protected void setTitleView(){
        if(mShowTitleView){
            titleView.setRightIcon(mRightIcon);
//            titleView.setOnRightClickListener(mRightClickListener);
            titleView.setTitle(mTitle);
        }else{
            titleView.setVisibility(View.GONE);
        }
    }

    protected abstract void initPre();
    /**findView要在init*/
    protected abstract void initData();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            onBack();
        }
        return true;
    }

    protected void onBack(){
        LogUtil.e(TAG,"onBack: finish");
        if(!isPreservable) {
            finish();
        }else{
            AppUtils.backToast(this,true);
        }
    }

    /**
     * 在编辑模式下的保存
     * @param isPreservable 可保存的。true:提示保存，false，直接返回
     */

}
