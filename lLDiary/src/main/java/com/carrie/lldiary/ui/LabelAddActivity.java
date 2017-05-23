package com.carrie.lldiary.ui;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.LabelAdapter;
import com.carrie.lldiary.base.BaseActivity;
import com.carrie.lldiary.dao.Diary;
import com.carrie.lldiary.entity.Label;
import com.carrie.lldiary.helper.DBHelper;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LabelAddActivity extends BaseActivity {
    @BindView(R.id.et_label)
    protected MaterialEditText etLabel;
    @BindView(R.id.label_recycler_view)
    protected RecyclerView recyclerView;
    private DBHelper dbHelper;
    private ArrayList<Label> list;
    private String mLabel;
    private Diary entity;
    private long id;

    @Override
    protected void initPre() {
        mLayoutId=R.layout.activity_label_add;
        TAG="LabelAddActivity";
        mRightIcon=R.drawable.btn_ok;
    }

    private void save(){
        mLabel=etLabel.getText().toString().trim();
        if(TextUtils.isEmpty(mLabel)){
            mLabel=getString(R.string.diary);//默认标签为【日记】
        }
        entity.setLabel(mLabel);
        dbHelper.updateDiary(entity);
        Toast.makeText(this,getString(R.string.save_success),Toast.LENGTH_SHORT).show();
        Intent data=new Intent();
        data.putExtra(DiaryListActivity.BUNDLE,entity);
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        entity = getIntent().getParcelableExtra(DiaryListActivity.BUNDLE);
        mLabel=entity.getLabel();
        etLabel.setText(mLabel);
        etLabel.setSelection(mLabel.length());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));

        list = new ArrayList<>();
        dbHelper = App.dbHelper;
        list =dbHelper.getLabels();
        final LabelAdapter adapter=new LabelAdapter(R.layout.item_label, list);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mLabel=list.get(position).label;
                etLabel.setText(mLabel);
                etLabel.setSelection(mLabel.length());
                adapter.setPressed(position);
            }
        }));

        int size=list.size();
        for(int i=0;i<size;i++){
            if(list.get(i).label.equals(mLabel)){
                adapter.setPressed(i);
                break;
            }
        }

        titleView.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }


}
