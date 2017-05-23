package com.carrie.lldiary.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.Emoji;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/5 0005.
 */
public class DialogIconView extends RelativeLayout{

    private static final String TAG ="DialogIconView" ;
    private EmojiconView emojiconView;
    private ArrayList<Emoji> icons;
    private TextView tv_title;
    private View view;

    public DialogIconView(Context context) {
        super(context);
        init(context);
    }

    public DialogIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialogIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        view = LayoutInflater.from(context).inflate(R.layout.view_dialog_icon,this);
        emojiconView = (EmojiconView) view.findViewById(R.id.emojiconView);
        tv_title = (TextView) view.findViewById(R.id.tv_select);

        icons= AppUtils.initList(icons);

    }

    public void setData(String prefix,int startIndex,int lastIndex,int pageSize){
        LogUtil.i(TAG,"333:startIndex="+startIndex+",lastIndex="+lastIndex+",pageSize="+pageSize);
        emojiconView.setData(prefix,startIndex,lastIndex,pageSize);
    }

    public void setTitle(String text){
        tv_title.setText(text);
    }

    public void setTitle(int res){
        tv_title.setText(res);
    }

    public void setLayoutHeight(int height){
        emojiconView.setLayoutHeight(height);
    }

    public int getCurrItem(){
        return emojiconView.getCurrItem();
    }

    public void setAlertDialog(AlertDialog dialog){
        emojiconView.mDialog=dialog;
    }

    public void setOnDialogItemClickListener(EmojiconView.OnDialogItemClickListener listener){
       emojiconView.setOnDialogItemClickListener(listener);
    }

    public void alertDialog(Activity activity){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        AlertDialog dialog = builder.show();
        dialog.setCancelable(false);
        setAlertDialog(dialog);
    }


}
