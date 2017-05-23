package com.carrie.lldiary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carrie.lldiary.R;

/**
 * Created by Administrator on 2016/7/10 0010.
 */
public class IconTextView extends RelativeLayout {

    private TextView textView,textView_float;
    private View view;

    public IconTextView(Context context) {
        super(context);
        init(context);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        view = LayoutInflater.from(context).inflate(R.layout.view_icon_text,this);
        textView = (TextView) view.findViewById(R.id.tv_text);
        textView_float = (TextView) view.findViewById(R.id.tv_float_text);

    }

    public void setText(String text){
        textView.setText(text);
    }

    public void setText(int res){
        textView.setText(res);
    }

    public void setTextSize(float size){
        textView.setTextSize(size);
    }

    public void setTextColor(int color){
        textView.setTextColor(color);
    }

    public void setFloatText(String text){
        textView_float.setText(text);
    }

    public void setFloatText(int res){
        textView_float.setText(res);
    }

    public void setFloatTextColor(int color){
        textView_float.setTextColor(color);
    }

    public void setFloatTextVisible(){
        textView_float.setVisibility(View.VISIBLE);
    }

    public void setImageBackground(int res){
        view.findViewById(R.id.iv_icon).setBackgroundResource(res);
    }
}
