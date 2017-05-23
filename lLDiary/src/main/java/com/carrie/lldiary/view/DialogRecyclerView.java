package com.carrie.lldiary.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.DialogGridAdapter;
import com.carrie.lldiary.entity.DiaryBg;
import com.carrie.lldiary.entity.ListDialog;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.umeng.message.proguard.T;

import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by new on 2017/4/14.
 */

public class DialogRecyclerView extends RelativeLayout {

    private TextView tvTitle;
    private RecyclerView recyclerView;
    private ArrayList<DiaryBg> mDiaryBgs;
    private ArrayList<ListDialog> mListDialog;
    private Context mContext;
    private View view;
    private AlertDialog dialog;

    public DialogRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public DialogRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialogRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext=context;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_recycler_view,this);
        tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_dialog);
        recyclerView.setHasFixedSize(true);

    }

    public void setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager){
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setList( @NonNull ArrayList<DiaryBg> list){
        this.mDiaryBgs=list;
    }

    public void setGridAdapter(){
        if(mDiaryBgs!=null&&mDiaryBgs.size()>0){
            DialogGridAdapter adapter=new DialogGridAdapter(mDiaryBgs);
            recyclerView.setAdapter(adapter);
        }
    }

    public void alertDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setView(view);
        dialog = builder.show();
    }

    public void cancelDialog(){
        if(dialog!=null){
            dialog.cancel();
        }
    }

    public void setTitle(int resId){
        tvTitle.setText(resId);
    }

    public void setOnItemClickListener(RecyclerItemClickListener.OnItemClickListener listener){
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, listener));
    }
}
