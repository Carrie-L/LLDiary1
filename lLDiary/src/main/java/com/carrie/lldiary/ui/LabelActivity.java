package com.carrie.lldiary.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.DiaryAdapter;
import com.carrie.lldiary.adapter.LabelAdapter;
import com.carrie.lldiary.base.BaseActivity;
import com.carrie.lldiary.dao.Diary;
import com.carrie.lldiary.entity.Label;
import com.carrie.lldiary.helper.DBHelper;
import com.carrie.lldiary.helper.DividerItemDecoration;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.helper.TestGestureListener;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.carrie.lldiary.ui.DiaryListActivity.BUNDLE;
import static com.carrie.lldiary.ui.DiaryListActivity.REQUEST_EDIT;

public class LabelActivity extends BaseActivity {
    @BindView(R.id.label_recycler_view)
    protected RecyclerView rvLable;
    @BindView(R.id.diary_recycler_view)
    protected RecyclerView rvDiary;
    @BindView(R.id.rl_container)
    protected RelativeLayout mLayout;
    private DBHelper dbHelper;
    private ArrayList<Label> labels;
    private ArrayList<Diary> diaries;
    private DiaryAdapter diaryAdapter;
    private LabelAdapter labelAdapter;
    private int mDiaryItemPos;//用来修改Diary的
    private int mLabelItemPos=0;//用来记录点击位置的
    private boolean isFirstIn=true;
    private GestureDetector mGestureDetector;

    @Override
    protected void initPre() {
        mLayoutId=R.layout.activity_label;
        TAG="LabelActivity";
        mTitle=getString(R.string.title_label);
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        dbHelper = App.dbHelper;

        setLabelList();
        setDiaryList();
    }

    private void setLabelList(){
        rvLable.setHasFixedSize(true);
        rvLable.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        setLabelMargin();
        labelAdapter = new LabelAdapter(R.layout.item_label,getLabels());
        rvLable.setAdapter(labelAdapter);
        onLabelItemClick();
    }

    private void setLabelMargin(){
        rvLable.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right=16;
            }
        });
    }

    private void setDiaryList(){
        rvDiary.setHasFixedSize(true);
        rvDiary.setLayoutManager(new LinearLayoutManager(this));
        rvDiary.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        diaries=dbHelper.getDiaries(labels.get(0).label);
        diaryAdapter = new DiaryAdapter(diaries);
        rvDiary.setAdapter(diaryAdapter);
        onDiaryItemClick();
    }

    private ArrayList<Label> getLabels(){
        labels = dbHelper.getLabels();
        return labels;
    }

    private void onLabelItemClick(){
        rvLable.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mLabelItemPos=position;
                labelAdapter.setPressed(position);
                diaries = dbHelper.getDiaries(labels.get(position).label);
                diaryAdapter.setList(diaries);
                LogUtil.i(TAG,"label="+labels.get(position).label+"，diaries="+diaries.toString());
            }
        }));
    }

    private void onDiaryItemClick(){
        rvDiary.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                isFirstIn=false;
                mDiaryItemPos = position;
                Intent intent = new Intent(LabelActivity.this, DiaryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Diary", diaries.get(position));
                startActivityForResult(intent, REQUEST_EDIT);
            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Diary diary = data.getParcelableExtra(BUNDLE);
            if (requestCode == REQUEST_EDIT) {
                LogUtil.i(TAG, "---REQUEST_EDIT---" + mDiaryItemPos);
                diaries.set(mDiaryItemPos, diary);
                diaryAdapter.notifyDataSetChanged();
                setResult(RESULT_OK,data);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isFirstIn){
            labelAdapter.setPressed(mLabelItemPos);
        }
    }







}
