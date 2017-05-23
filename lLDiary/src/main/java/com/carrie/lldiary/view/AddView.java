package com.carrie.lldiary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.AddListAdapter;

/**
 * Created by Administrator on 2016/6/5 0005.
 */
public class AddView extends RelativeLayout {

    private EditText editText;
    private EditText et_balance;
    private ImageButton ib;

    public AddView(Context context) {
        super(context);

        init(context);
    }

    public AddView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public AddView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_add, this);
        editText = (EditText) view.findViewById(R.id.et_add);
        et_balance = (EditText) view.findViewById(R.id.et_add_balance);
        ib = (ImageButton) view.findViewById(R.id.ib_add);
    }

    /**
     * 设置输入框的值
     *
     * @param text
     */
    public void setEditText(String text) {
        editText.setText(text);
    }

    /**
     * 设置输入框的值
     *
     * @param resString
     */
    public void setEditText(int resString) {
        editText.setText(getContext().getResources().getString(resString));
    }

    /**
     * 输入余额
     * @param text
     */
    public void setEditTextBalance(String text) {
        et_balance.setText(text);
    }

    public void setEditTextBalance(int resString) {
        et_balance.setText(getContext().getResources().getString(resString));
    }

    public void setSelection(int length){
        editText.setSelection(length);
    }

    public void setSelectionBalance(int length){
        et_balance.setSelection(length);
    }

    public String getString(){
        return editText.getText().toString().trim();
    }

    public String getBalance(){
        return et_balance.getText().toString().trim();
    }

    public void showBalance(boolean isShowBalance){
        if(isShowBalance){
            et_balance.setVisibility(VISIBLE);
        }
    }

    public void setBackground(int resDrawable){
        ib.setBackgroundResource(resDrawable);
    }

    public void setOnClickListener(OnClickListener listener){
        ib.setOnClickListener(listener);
    }

    public void addTextChangedListener(AddListAdapter.MutableWatcher watcher){
        editText.addTextChangedListener(watcher);
    }


}
