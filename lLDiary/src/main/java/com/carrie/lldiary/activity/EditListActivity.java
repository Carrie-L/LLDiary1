package com.carrie.lldiary.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.AddListAdapter;
import com.carrie.lldiary.base.BaseActivity;
import com.carrie.lldiary.base.MyBaseActivity;
import com.carrie.lldiary.entity.ListDialog;
import com.carrie.lldiary.fab.AddFloatingActionButton;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.DialogUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.DialogIconView;
import com.carrie.lldiary.view.TitleView;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by Administrator on 2016/6/4 0004.
 */
public class EditListActivity extends BaseActivity {
    private static final String TAG = "EditListActivity";
    private ArrayList<ListDialog> lists;
    private AddListAdapter adapter;

    private int startIndex, lastIndex, pageSize;
    private String type;

    @BindView(R.id.rv_add)
    protected RecyclerView recyclerView;
    @BindView(R.id.fab_add_line)
    protected AddFloatingActionButton fab_add;

    @Override
    protected void initPre() {
        mLayoutId = R.layout.activity_edit_list;
        Intent intent = getIntent();
        startIndex = intent.getIntExtra("StartIndex", 0);
        lastIndex = intent.getIntExtra("LastIndex", 0);
        pageSize = intent.getIntExtra("PageSize", 0);
        type = intent.getStringExtra("Type");
        LogUtil.i(TAG, "111:startIndex=" + startIndex + ",lastIndex=" + lastIndex + ",pageSize=" + pageSize);
        mTitle = intent.getStringExtra("Title");
        mRightIcon = R.drawable.btn_ok;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();


        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        initList();

        DialogIconView iconView = new DialogIconView(this);
        iconView.setData("emoji_e_", startIndex, lastIndex, pageSize);
        LogUtil.i(TAG, "222:startIndex=" + startIndex + ",lastIndex=" + lastIndex + ",pageSize=" + pageSize);
        iconView.setTitle(getString(R.string.title_select_icon));

        if (adapter == null) {
            adapter = new AddListAdapter(lists, this, iconView, type);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFirstLine();
                adapter.notifyDataSetChanged();
            }
        });

        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                lists.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void initList() {
        lists = AppUtils.initList(lists);
        initFirstLine();

        AppUtils.logArrayListToString(lists);


    }

    /**
     * 初始化，第一行
     */
    private void initFirstLine() {
        ListDialog listDialog = new ListDialog();
        listDialog.icon = "icon_plus";
        listDialog.name = "";
        lists.add(listDialog);
    }

    private void insertClassify() {
        App.dbHelper.insertClassifyAll(lists);
    }

    private void insertAccount() {
        App.dbHelper.insertAccountAll(lists);
    }

    private boolean backToast() {
        int size = lists.size();
        for (int i = 0; i < size; i++) {
            if (!TextUtils.isEmpty(lists.get(i).name)) {
                DialogUtils.alertMDesignDialog(EditListActivity.this, "", getString(R.string.dialog_back));
                return true;
            }
        }
        return false;
    }

    private boolean okToast(String name, boolean bool) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, getResources().getString(R.string.toast_edit_not_null), Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!bool) {
            Toast.makeText(this, "[" + name + "]" + getResources().getString(R.string.toast_is_existed), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LogUtil.i(TAG, "按下返回键");
            if (!backToast()) {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

   private void rightClick(){
       titleView.setOnRightClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int size = lists.size();
               LogUtil.i(TAG, "LISTS=" + size + "，list.toString=" + lists.toString());

               if (type.equals("Classify")) {
                   for (int i = 0; i < size; i++) {
                       boolean isReturn = okToast(lists.get(i).name, App.dbHelper.isClassifyUnique(lists.get(i).name));
                       if (isReturn) {
                           return;
                       }
                   }
                   insertClassify();
               } else if (type.equals("Account")) {
                   for (int i = 0; i < size; i++) {
                       boolean isReturn = okToast(lists.get(i).name, App.dbHelper.isAccountUnique(lists.get(i).name));
                       if (isReturn) {
                           return;
                       }
                   }
                   insertAccount();
               }
               finish();
           }
       });
   }
}
