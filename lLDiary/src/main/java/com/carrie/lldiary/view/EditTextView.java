package com.carrie.lldiary.view;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import static android.R.attr.max;

/**
 * Created by Administrator on 2016/7/10 0010.
 * EditActivity  textView-editText 模式
 */
public class EditTextView extends RelativeLayout{
    private static final String TAG = "EditTextView";
    private MaterialEditText materialEditText,materialEditTextRight;
    private TextView textView;


    public boolean isEdited=false;

    public EditTextView(Context context) {
        super(context);
        init(context);
    }

    public EditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.view_textview_edittext,this);
        textView = (TextView) view.findViewById(R.id.tv_text);
        materialEditText = (MaterialEditText) view.findViewById(R.id.material_edit_text_vertical);
        materialEditTextRight = (MaterialEditText) view.findViewById(R.id.material_edit_text_right);

        materialEditText.addTextChangedListener(new TextWatcher() {
            private String editor;
            private int initStart=-1;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                LogUtil.i(TAG,"beforeTextChanged:s="+s.toString()+",start="+start+",count="+count+",after="+after);
                if(after<10){
                    if(after>0&&!TextUtils.isEmpty(s.toString())){
                        editor= s.toString();
                        initStart=1;
                    }else if(after==0&&TextUtils.isEmpty(s.toString())){
                        initStart=0;
                    }

                    LogUtil.i(TAG,"initStart="+initStart);
                }



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtil.i(TAG,"afterTextChanged:s="+s.toString());
                    if(initStart==0&&!TextUtils.isEmpty(s.toString())){
                        isEdited=true;
                    }else if(initStart==1&&!s.toString().equals(editor)){
                        isEdited=true;
                    }
            }
        });

    }

    public void setBaseColor(int color){
        materialEditText.setBaseColor(color);
        materialEditTextRight.setBaseColor(color);

        materialEditText.setUnderlineColor(color);
        materialEditTextRight.setBaseColor(color);
    }

    public void setText(String text){
        textView.setText(text);
    }

    public void setText(int res){
        textView.setText(res);
    }

    public void setTextSize(int size){
        textView.setTextSize(size);
    }

    public void setEditText(String text){
        materialEditText.setText(text);
    }

    public void setEditText(int res){
        materialEditText.setText(res);
    }

    public void setEditTextRight(String text){
        materialEditTextRight.setText(text);
    }

    public void setEditTextRight(int res){
        materialEditTextRight.setText(res);
    }

    public void setEditTextSize(int size){
        materialEditText.setTextSize(size);
    }

    public void setEditRightTextSize(int size){
        materialEditTextRight.setTextSize(size);
    }

    public void setRightVisible(){
        materialEditTextRight.setVisibility(View.VISIBLE);
    }

    public String getEditText(){
        return materialEditText.getText().toString().trim();
    }

    public String getEditTextRight(){
        return materialEditTextRight.getText().toString().trim();
    }

    public void setHintText(int res){
        materialEditText.setHint(res);
    }

    public void setHintTextRight(int res){
        materialEditTextRight.setHint(res);
    }

    public void setHintColor(int color){
        materialEditText.setHintTextColor(color);
    }

    public void setLongPress(boolean bool){
        materialEditText.setLongClickable(bool);
    }

    public void setLongPresRight(boolean bool){
        materialEditTextRight.setLongClickable(bool);
    }

    public void setSelection(int length){
        materialEditText.setSelection(length);
    }

    /**
     * 弹出选择图标对话框
     * @param iconView
     * @param startIndex 图标名称开始位置
     * @param lastIndex
     * @param pageSize
     */
    public void setIconDialog(final DialogIconView iconView,final int startIndex,final int lastIndex,final int pageSize){
        materialEditText.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(materialEditText.isLongClickable()){
                    iconView.setData("emoji_e_",startIndex,lastIndex,pageSize);
                    LogUtil.i(TAG,"222:startIndex="+startIndex+",lastIndex="+lastIndex+",pageSize="+pageSize);
                    iconView.setTitle(R.string.title_select_icon);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(iconView);
                    AlertDialog dialog = builder.show();
                    dialog.setCancelable(false);
                    iconView.setAlertDialog(dialog);
                }
                return true;
            }
        });

    }



    /**
     * 设置浮动标签
     * @param model  MaterialEditText.FLOATING_LABEL_NORMAL|MaterialEditText.FLOATING_LABEL_HIGHLIGHT|MaterialEditText.FLOATING_LABEL_NONE
     */
    public void setFloatLabel(int model){
        materialEditText.setFloatingLabel(model);
    }

    public void setFloatLabelText(String text){
        materialEditText.setFloatingLabelText(text);
    }

    public void setFloatingLabelAlwaysShown(boolean bool){
        materialEditText.setFloatingLabelAlwaysShown(bool);
    }

    public void setFloatLabelRight(int model){
        materialEditTextRight.setFloatingLabel(model);
    }

    public void setFloatLabelTextRight(String text){
        materialEditTextRight.setFloatingLabelText(text);
    }

    public void setFloatingLabelAlwaysShownRight(boolean bool){
        materialEditTextRight.setFloatingLabelAlwaysShown(bool);
    }

    public void setInputType(int type){
        materialEditText.setInputType(type);
    }

    public void setInputTypePassword(){
        materialEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    /**
     * 输入框下方的帮助文字
     * @param helpText
     */
    public void setHelpText(String helpText){
        materialEditText.setHelperText(helpText);
    }

    /**
     *是否显示清除按钮
     * @param bool
     */
    public void showClearButton(boolean bool){
        materialEditText.setShowClearButton(bool);
    }

    /**
     * 设置最多几个字符(只会显示在underLine右下角,如最大长度为3，则显示 0/3 ;0为输入的字符数，并不限制字符数)
     * @param maxCharacters 提示最多多少个字符
     */
    public void setMaxCharacters(int maxCharacters){
        materialEditText.setMaxCharacters(maxCharacters);
    }

    /**
     * 设置最少几个字符
     * @param minCharacters
     */
    public void setMinCharacters(int minCharacters){
        materialEditText.setMinCharacters(minCharacters);
    }

    /**
     * //限制最多maxCharacters个字符
     * @param max 最多字符数
     */
    public void setEditTextLength(int max){
        InputFilter[] filters = {new InputFilter.LengthFilter(max)};
        materialEditText.setFilters(filters);
    }

    public void setIconLeft(int res){
        materialEditText.setIconLeft(res);
    }

    public void setIconRight(int res){
        materialEditText.setIconRight(res);
    }

    public void alertIconDialog(){
        materialEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e(TAG,"点击了editText，弹出对话框");
            }
        });
    }

    /**
     * 日期选择对话框 Only日期
     */
    public void alertDateDialog(){
        materialEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e(TAG,"点击了editText，弹出对话框");

                DateUtil.datePickerDialog(getContext());
                DateUtil.setOnDateClickListener(new DateUtil.OnDateClickListener() {
                    @Override
                    public void getDate(String date) {
                        LogUtil.e(TAG,"OnDateClickListener: date="+date);
                        materialEditText.setText(date);
                    }
                });
            }
        });
    }

    /**
     * 日期选择对话框：日期+星期
     */
    public void alertDateWeekDialog(){
        materialEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e(TAG,"点击了editText，弹出对话框");

                DateUtil.dateWeekPickerDialog(getContext());
                DateUtil.setOnDateClickListener(new DateUtil.OnDateClickListener() {
                    @Override
                    public void getDate(String date) {
                        LogUtil.e(TAG,"OnDateClickListener: date="+date);
                        materialEditText.setText(date);
                    }
                });
            }
        });
    }

    /**
     * 时间选择对话框：时+分
     */
    public void alertTimeDialog(){
        materialEditTextRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e(TAG,"materialEditTextRight，弹出对话框");

                DateUtil.timePickerDialog(getContext());
                DateUtil.setOnTimeClickListener(new DateUtil.OnTimeClickListener() {
                    @Override
                    public void getTime(String time) {
                        LogUtil.e(TAG,"OnTimeClickListener: time="+time);
                        materialEditTextRight.setText(time);
                    }
                });
            }
        });
    }

    public void setFocusMode(boolean bool){
        materialEditText.setFocusableInTouchMode(bool);
    }
    public void setFocusModeRight(boolean bool){
        materialEditTextRight.setFocusableInTouchMode(bool);
    }


    public void addTextChangeListener(TextWatcher textWatcher){
        materialEditText.addTextChangedListener(textWatcher);
    }
}
