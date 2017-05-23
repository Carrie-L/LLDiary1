package com.carrie.lldiary.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.AddListAdapter2;
import com.carrie.lldiary.base.BaseActivity;
import com.carrie.lldiary.entity.ListDialog;
import com.carrie.lldiary.fab.AddFloatingActionButton;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddListActivity extends BaseActivity {
    public static final Integer TYPE_ACCOUNT=1;
    public static final Integer TYPE_CLASSIFY=2;
    @BindView(R.id.rv_add)
    protected RecyclerView recyclerView;
    @BindView(R.id.fab_add_line)
    protected AddFloatingActionButton fab_add;
    private int type;
    private ListDialog listDialog;
    private ArrayList<ListDialog> list;
    private AddListAdapter2 adapter;

    @Override
    protected void initPre() {
        mLayoutId=R.layout.activity_add_list;

        Intent intent=getIntent();
        type= intent.getIntExtra("Type",0);
        mTitle=intent.getStringExtra("Title");
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        setList();
        adapter = new AddListAdapter2(list);
        recyclerView.setAdapter(adapter);

        addLine();
    }

    private void setList(){
        if(type==TYPE_ACCOUNT){
            listDialog = new ListDialog("","icon_money","");
        }else{
            listDialog = new ListDialog("","emoji_icecream",null);
        }

        list.add(listDialog);
        LogUtil.i(TAG,"list="+list.size());
    }

    private void addLine(){
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setList();
                adapter.notifyItemInserted(list.size()-1);
            }
        });
    }


}
