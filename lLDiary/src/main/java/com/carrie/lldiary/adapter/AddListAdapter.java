package com.carrie.lldiary.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.ListDialog;
import com.carrie.lldiary.helper.ItemTouchHelperAdapter;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.AddView;
import com.carrie.lldiary.view.DialogIconView;
import com.carrie.lldiary.view.EmojiconView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/5 0005.
 * 动态增加一行
 */
public class AddListAdapter extends RecyclerView.Adapter<AddListAdapter.AddViewHolder> implements ItemTouchHelperAdapter{
    private static final String TAG = "AddListAdapter";
    private ArrayList<ListDialog> lists;
    private Context mContext;
    private AlertDialog mDialog;
    private DialogIconView mIconView;
    private View view;
    private String mType;
    Resources resources;


    public AddListAdapter(ArrayList<ListDialog> list, Context context, DialogIconView iconView,String type) {
        this.lists = list;
        this.mContext = context;
        this.mIconView = iconView;
        this.mType=type;
        resources=context.getResources();
    }

    @Override
    public AddViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_view, viewGroup, false);
        return new AddViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddViewHolder addViewHolder, int i) {
        final int pos = i;

        addViewHolder.watch.setActive(false);
        addViewHolder.addView.setEditText(lists.get(i).name);
        addViewHolder.addView.setBackground(resources.getDrawable(AppUtils.getResourceIdentifier(resources,lists.get(i).icon,"drawable")));

        if(mType.equals("Account")){
            addViewHolder.addView.showBalance(true);
            addViewHolder.addView.setEditTextBalance(lists.get(i).balance);
        }

        addViewHolder.watch.setPosition(pos);
        addViewHolder.watch.setActive(true);
        addViewHolder.watch.setViewHolder(addViewHolder);

        addViewHolder.addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(mIconView);
                mDialog = builder.show();
                mDialog.setCancelable(false);
                mIconView.setAlertDialog(mDialog);

                if (!mDialog.isShowing()) {
                    ((ViewGroup) mIconView.getParent()).removeView(mIconView);
                    return;
                }

                mIconView.setOnDialogItemClickListener(new EmojiconView.OnDialogItemClickListener() {
                    @Override
                    public void selectIcon(int resId) {
                        LogUtil.e(TAG, "icon=" + resId);
                        ((ViewGroup) mIconView.getParent()).removeView(mIconView);
                        addViewHolder.addView.setBackground(resId);

                       ListDialog listDialog=new ListDialog();
                        listDialog.icon = resources.getResourceName(resId);
                        listDialog.name = addViewHolder.addView.getString();
                        if(mType.equals("Account")){
                            listDialog.balance=addViewHolder.addView.getBalance();
                        }
                        lists.set(pos,listDialog);
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public class AddViewHolder extends RecyclerView.ViewHolder {
        private AddView addView;
        private MutableWatcher watch;

        public AddViewHolder(View itemView) {
            super(itemView);
            addView = (AddView) itemView.findViewById(R.id.addView);
            watch = new MutableWatcher();
            addView.addTextChangedListener(watch);
        }
    }

    public class MutableWatcher implements TextWatcher {

        private int mPosition;
        private boolean mActive;
        private AddViewHolder mHolder;

        void setPosition(int position) {
            mPosition = position;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        void setViewHolder(AddViewHolder holder) {
            mHolder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                ListDialog listDialog=new ListDialog();
                listDialog.icon = lists.get(mPosition).icon;
                listDialog.name =mHolder.addView.getString();
                if(mType.equals("Account")){
                    listDialog.balance=mHolder.addView.getBalance();
                }
                lists.set(mPosition, listDialog);
            }
        }
    }

}
