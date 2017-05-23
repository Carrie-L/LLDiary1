package com.carrie.lldiary.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.ListDialogAdapter;
import com.carrie.lldiary.entity.ListDialog;
import com.carrie.lldiary.fab.AddFloatingActionButton;
import com.carrie.lldiary.helper.DividerItemDecoration;
import com.carrie.lldiary.helper.OnItemMoveListener;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.helper.SimpleItemTouchHelperCallback;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by Carrie on 2016/6/3 0003.
 * 列表对话框
 */
public class ListDialogView extends LinearLayout{

    private static final String TAG ="ListDialogView" ;
    private TextView tv_select;
    private AddFloatingActionButton fab_add;
    private RecyclerView recyclerView;
    private ArrayList<ListDialog> lists;
    private ListDialogAdapter adapter;
    private Context mContext;

    public ListDialogView(Context context) {
        super(context);
        mContext=context;

        initView();
    }

    public ListDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initView();
    }

    public ListDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initView();
    }

    private void initView(){
        View view= LayoutInflater.from(mContext).inflate(R.layout.view_list_dialog,this);
        tv_select = (TextView) view.findViewById(R.id.tv_select);
        fab_add = (AddFloatingActionButton) view.findViewById(R.id.fab_add);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_dialog);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
    }

    public void setListAdapter(ArrayList<ListDialog> list){
        lists=list;

        if(adapter==null){
            adapter = new ListDialogAdapter(mContext,lists);
            recyclerView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    public void canMoveAndRemove(boolean can){
        if(can){
            ItemTouchHelper.Callback callback=new SimpleItemTouchHelperCallback(adapter);
            ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    public void setTitle(String text){
        tv_select.setText(text);
    }

    public void setTextSelect(int resString){
        tv_select.setText(getContext().getResources().getString(resString));
    }

    public void setFabAddShow(){
       fab_add.setVisibility(View.VISIBLE);
    }

    public void setOnAddClickListener(OnClickListener listener){
        fab_add.setOnClickListener(listener);
    }

    public void setOnItemClickListener(RecyclerItemClickListener.OnItemClickListener listener){
        adapter.setOnItemClickListener(listener);
    }

    public void setOnItemMoveListener(OnItemMoveListener listener){
        adapter.setOnItemMoveListener(listener);
    }

    public void setArrayList(ArrayList<ListDialog> list){
        lists=list;
        LogUtil.i(TAG,"lists="+lists.size()+","+lists.toString());
    }


}
