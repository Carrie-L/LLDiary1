package com.carrie.lldiary.ui;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.DiaryAdapter;
import com.carrie.lldiary.base.BaseActivity;
import com.carrie.lldiary.dao.Diary;
import com.carrie.lldiary.fab.FloatingActionButton;
import com.carrie.lldiary.helper.DBHelper;
import com.carrie.lldiary.helper.DividerItemDecoration;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.helper.SimpleItemTouchHelperCallback;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.carrie.lldiary.App.dbHelper;
import static com.carrie.lldiary.activity.PlanActivity.entities;

/**
 * Created by new on 2017/4/14.
 * 日记列表
 */

public class DiaryListActivity extends BaseActivity {
    @BindView(R.id.fab_add_diary)
    protected FloatingActionButton fabAdd;
    @BindView(R.id.recycler_view_diary)
    protected RecyclerView recyclerView;
    private ArrayList<Diary> list;

    public static final Integer REQUEST_EDIT = 0;
    public static final Integer REQUEST_ADD = 1;
    public static final String BUNDLE = "Diary";
    private DiaryAdapter adapter;
    private int mPos;
    private DBHelper dbHelper;

    @Override
    protected void initPre() {
        mLayoutId = R.layout.activity_diary_list;
        mTitle = getString(R.string.my_diary);
        titleView.setRightIcon(R.drawable.ic_search);
        TAG = "DiaryListActivity";
        titleView.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

    }

    // TODO: 2017/4/17  limit 20个 加载更多.Bug:标签列表修改时，这个列表没有改变

    private void search() {

    }

    private void onLabel(){
        Intent intent=new Intent(this, LabelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent,REQUEST_EDIT);
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        getList();
        LogUtil.i(TAG, "list=" + list.size());
        adapter = new DiaryAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mPos = position;
                Intent intent = new Intent(DiaryListActivity.this, DiaryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Diary", list.get(position));
                startActivityForResult(intent, REQUEST_EDIT);
            }
        }));

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryListActivity.this, DiaryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, REQUEST_ADD);
            }
        });
        swipe();

        if(list.size()>0){
            titleView.setRightIcon1(R.drawable.ic_label);
            titleView.setOnRightClickListener1(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLabel();
                }
            });
        }
    }

    private void getList() {
        dbHelper = App.dbHelper;
        list = dbHelper.getDiaryAllList();
    }

    private void swipe() {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                dbHelper.deleteDiaryItem(list.get(pos));
                list.remove(pos);
                adapter.notifyItemRemoved(pos);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setAlpha(SimpleItemTouchHelperCallback.ALPHA_FULL);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    final float alpha = SimpleItemTouchHelperCallback.ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Diary diary = data.getParcelableExtra(BUNDLE);
            if (requestCode == REQUEST_EDIT) {
                LogUtil.i(TAG, "---REQUEST_EDIT---" + mPos);
                list.set(mPos, diary);
            } else if (requestCode == REQUEST_ADD) {
                LogUtil.i(TAG, "---REQUEST_ADD---");
                list.add(0,diary);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
