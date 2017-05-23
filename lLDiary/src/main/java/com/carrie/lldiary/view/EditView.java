package com.carrie.lldiary.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.ListDialog;
import com.carrie.lldiary.helper.OnItemMoveListener;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.umeng.fb.util.Res;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/3 0003.
 * 左边标签，右边输入框
 */
public class EditView extends RelativeLayout {
    private static final String TAG="EditView";
    private static final int UPDATE_UI = 0;
    private MaterialEditText editText;
    private TextView textView;
    private ImageButton ib;
    /**ImageButton属性：0=删除；1=选择*/
    private int mProperty=0;
    private String mTitle;
    private String mAddText;
    private ListDialogView dialogView;
    private AlertDialog mDialog;
//    private Context mContext;
    private ArrayList<ListDialog> lists;
    private TextView tv_select_text;
    private String mSeleteText;
    private int mResId;
    private String  mDrawableName;
    private ImageView icon;
    private RelativeLayout rl_select;

    public boolean isEdited=false;


    public EditView(Context context) {
        super(context);
        initView();
    }

    public EditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_edit_text,this);
        editText = (MaterialEditText) view.findViewById(R.id.et);
        textView = (TextView) view.findViewById(R.id.tv);
        ib = (ImageButton) view.findViewById(R.id.ib);
        tv_select_text = (TextView) view.findViewById(R.id.tv_money_select);
        icon = (ImageView) view.findViewById(R.id.iv_icon);
        rl_select = (RelativeLayout) view.findViewById(R.id.rl);

        editText.addTextChangedListener(new TextWatcher() {
            private String editor;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if(editText.getId()!=R.id.etv_ann_date){
                    if(!TextUtils.isEmpty(s.toString())&&after>0){
                        editor=s.toString();
                    }
//                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(editor)){
                    isEdited=true;
                }
            }
        });
    }


    public void setInputType(int type){
        editText.setInputType(type);
        LogUtil.i(TAG,"--setInputType--");
    }

    /**
     * 设置输入框的值
     * @param text
     */
    public void setEditText(String text){
        if(text==null){
            editText.setText("");
        }else{
            editText.setText(text);
        }
    }

    /**
     * 设置输入框的值
     * @param resString
     */
    public void setEditText(int resString){
        editText.setText(getContext().getResources().getString(resString));
    }

    /**
     * 设置左侧标签的值
     * @param text
     */
    public void setTvText(String text){
        textView.setText(text);
    }

    /**
     * 设置左侧标签的值
     * @param resString
     */
    public void setTvText(int resString){
        textView.setText(getContext().getResources().getString(resString));
    }

    /**
     * 设置选择的值
     * @param text
     */
    public void setSelectText(String text){
        if(text==null){
            mSeleteText="";
            tv_select_text.setText("");
        }else{
            mSeleteText=text;
            tv_select_text.setText(text);
        }

    }

    public String getSelectText(){
        return mSeleteText;
    }

    public void setIcon(int resId){
        mResId=resId;
        icon.setVisibility(View.VISIBLE);
        icon.setBackgroundResource(getResources().getIdentifier(resId+"","drawable","com.carrie.lldiary"));
    }

    public void setIcon(String name, Resources resources){
        mDrawableName=name;
        mResId=AppUtils.getResourceIdentifier(resources,name,"drawable");
        icon.setVisibility(View.VISIBLE);
        icon.setImageResource(mResId);
    }


    public int getIcon(){
        return mResId;
    }

    public String getIconName(){
        return  mDrawableName;
    }

    /**
     * 设置选择的值
     * @param resString
     */
    public void setSelectText(int resString){
        tv_select_text.setText(getContext().getResources().getString(resString));
    }

    public void setSelection(int length){
        editText.setSelection(length);
    }

    public String getString(){
        return editText.getText().toString().trim();
    }

    public String getEditText(){
        return editText.getText().toString().trim();
    }

    public void setBackground(int resDrawable){
        ib.setBackgroundResource(resDrawable);
    }

    public void setNextFocusDown(int id){
        editText.setNextFocusDownId(id);
    }

    /**
     *
     * @param imeOptions EditorInfo.IME_ACTION_DONE|EditorInfo.IME_ACTION_ENTER
     */
    public void setImeOptions(int imeOptions){
        editText.setImeOptions(imeOptions);
    }

    /**
     * 设置ImageButton是 X 还是 >
     * 0=删除；1=选择
     */
    public void setProperty(int property){
        mProperty=property;

        if(mProperty==1){
            rl_select.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
            ib.setBackgroundResource(R.drawable.white_arrow);
        }
    }

    public void setOnClickListener(OnClickListener listener){
        if(mProperty==0){
            //delete
            ib.setOnClickListener(listener);
        }else{
            //select
            rl_select.setOnClickListener(listener);
            ib.setOnClickListener(listener);
        }
    }

    public void alertDialog(){
        dialogView = new ListDialogView(getContext());
        dialogView.setTitle(getTitle());

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        mDialog=builder.show();
           }



    public void setOnItemClickListener(RecyclerItemClickListener.OnItemClickListener listener){
        if(dialogView!=null){
            dialogView.setOnItemClickListener(listener);
        }
    }

    public void setOnItemMoveListener(OnItemMoveListener listener){
        if(dialogView!=null){
            dialogView.setOnItemMoveListener(listener);
        }
    }

    public void dialogDismiss(){
        mDialog.dismiss();
    }

    public void setOnAddClickListener(OnClickListener listener){
        if(dialogView!=null){
            dialogView.setOnAddClickListener(listener);
        }
    }

    public void setTitle(String title){
        mTitle= title;
    }


    public String getTitle(){
        return mTitle;
    }

    public void setTextAdd(String text){
        mAddText=text;
    }

    public String getText(){
        return mAddText;
    }

    public void setArrayList(ArrayList<ListDialog> list){
        lists=list;
    }

    public void  setListAdapter(ArrayList<ListDialog> list){
        if(dialogView!=null){
            dialogView.setListAdapter(list);
        }
    }

    public void canMoveAndRemove(boolean can){
        dialogView.canMoveAndRemove(can);
    }

    public void setFabAddShow(){
        dialogView.setFabAddShow();
    }

    public void setSingleLine(){
        editText.setSingleLine();
        editText.setMaxLines(1);
    }

}
