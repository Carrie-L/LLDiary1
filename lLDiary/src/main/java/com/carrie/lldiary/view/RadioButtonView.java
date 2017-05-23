package com.carrie.lldiary.view;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.utils.LogUtil;

/**
 * Created by Administrator on 2016/6/3 0003.
 */
public class RadioButtonView extends RelativeLayout {

    private static final String TAG = "RadioButtonView";
    private TextView tv;
    private RadioButton rb_left,rb_center,rb_right;

    //-1:支出；1;收入; 0 转账
    private int mState;
    private RadioGroup radioGroup;
    private static final int LEFT=-1;
    private static final int CENTER=0;
    private static final int RIGHT=1;

    public RadioButtonView(Context context) {
        super(context);
        initView();
    }

    public RadioButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RadioButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        View view= LayoutInflater.from(getContext()).inflate(R.layout.view_check_box,this);
        tv = (TextView) view.findViewById(R.id.tv);
        rb_left = (RadioButton) view.findViewById(R.id.rb_left);
        rb_center = (RadioButton) view.findViewById(R.id.rb_center);
        rb_right = (RadioButton) view.findViewById(R.id.rb_right);
        radioGroup = (RadioGroup) view.findViewById(R.id.rg);
    }

    public void setTvText(String text){
        tv.setText(text);
    }

    public void setTvText(int resString){
        tv.setText(getContext().getResources().getString(resString));
    }

    public void setLeftText(int resString){
        rb_left.setText(getContext().getResources().getString(resString));
    }


    public void setCenterText(int resString){
        rb_center.setVisibility(View.VISIBLE);
        rb_center.setText(getContext().getResources().getString(resString));
    }

    public void setRightText(int resString){
        rb_right.setText(getContext().getResources().getString(resString));
    }

    public void onRadioButtonClicked(int id){
        switch (id){
            case R.id.rb_left:
                    mState=1;
                    LogUtil.i(TAG,"rb_left checked: "+mState);
                break;
            case R.id.rb_center:
                    mState=-1;
                    LogUtil.i(TAG,"rb_center checked ");
                break;
            case R.id.rb_right:
                    mState=0;
                    LogUtil.i(TAG,"rb_right checked :" +mState);
                break;
        }
    }


    public void setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener listener){
        radioGroup.setOnCheckedChangeListener(listener);
    }

    /**
     * 左边：收入
     * @param bool
     */
    public void setCheckedLeft(boolean bool){
        rb_left.setChecked(bool);
        mState=LEFT;
    }

    /**
     * 中间：支出
     * @param bool
     */
    public void setCheckedCenter(boolean bool){
        rb_center.setChecked(bool);
        mState=CENTER;
    }

    /**
     * 右边：转账
     * @param bool
     */
    public void setCheckedRight(boolean bool){
        rb_right.setChecked(bool);
        mState=RIGHT;
    }

    public int getState(){
        return mState;
    }

    public void setOnChangeListenerLeft(CompoundButton.OnCheckedChangeListener listener){
        rb_left.setOnCheckedChangeListener(listener);
    }

    public void setOnChangeListenerCenter(CompoundButton.OnCheckedChangeListener listener){
        rb_center.setOnCheckedChangeListener(listener);
    }

    public void setOnChangeListenerRight(CompoundButton.OnCheckedChangeListener listener){
        rb_right.setOnCheckedChangeListener(listener);
    }

    public void setOnChangeListener(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
               switch (checkedId){
                   case R.id.rb_left:
                       rb_left.setChecked(true);
                       mState=LEFT;
                       break;
                   case R.id.rb_center:
                       rb_center.setChecked(true);
                       mState=CENTER;
                       break;
                   case R.id.rb_right:
                       rb_right.setChecked(true);
                       mState=RIGHT;
                       break;
               }
            }
        });
    }
}
