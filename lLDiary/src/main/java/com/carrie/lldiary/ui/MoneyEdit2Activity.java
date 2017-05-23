package com.carrie.lldiary.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.EditListActivity;
import com.carrie.lldiary.activity.MoneyEditActivity;
import com.carrie.lldiary.base.BaseActivity;
import com.carrie.lldiary.dao.Money;
import com.carrie.lldiary.dao.MoneyAccount;
import com.carrie.lldiary.dao.MoneyClassify;
import com.carrie.lldiary.entity.ListDialog;
import com.carrie.lldiary.helper.DBHelper;
import com.carrie.lldiary.helper.OnItemMoveListener;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.DialogRecyclerView;
import com.carrie.lldiary.view.EditTextView;
import com.carrie.lldiary.view.EditView;
import com.carrie.lldiary.view.RadioButtonView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;
import static com.carrie.lldiary.App.dbHelper;

public class MoneyEdit2Activity extends BaseActivity {
    @BindView(R.id.et_money_content)
    protected EditView evContent;
    @BindView(R.id.et_money_account)
    protected EditView evAccount;
    @BindView(R.id.et_money_classify)
    protected EditView evClassify;
    @BindView(R.id.et_money_price)
    protected EditView evPrice;
    @BindView(R.id.et_money_date)
    protected EditView evDate;
    @BindView(R.id.rb_money_state)
    protected RadioButtonView rbvStatus;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private DBHelper dbHelper;
    private Resources resources;
    private String mType;
    private ArrayList<ListDialog> listsClassify;
    private ArrayList<ListDialog> listsAccount;
    private String iconAccount;
    private String iconClassify;
    private Money money;
    private Integer mPos=0;
    private String price;
    private long id;

    @Override
    protected void initPre() {
        mLayoutId = R.layout.activity_money_edit2;
        mTitle = getString(R.string.title_activity_money_edit);
        mRightIcon = R.drawable.btn_ok;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        dbHelper = App.dbHelper;
        resources = getResources();
        money=new Money();
        init();
        initDB();
        initEditData();
        setOnClick();

        titleView.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                Intent intent=new Intent();
                intent.putExtra("Money",money);
                intent.putExtra("Pos",mPos);
                LogUtil.i(TAG,"mPos="+mPos);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void save() {
        price = evPrice.getEditText();
        evContent.setSelection(evContent.getEditText().length());
        evPrice.setSelection(price.length());
        DecimalFormat decimal = new DecimalFormat("#.##");
        price = decimal.format(Double.valueOf(price));

        //Long id, String classifyIcon, String accountIcon, String classify, String account, String content, String income, String expense, String price, int state, String date
        money = new Money(money==null?null:money.getId(), evClassify.getIconName(), evAccount.getIconName(), evClassify.getSelectText(), evAccount.getSelectText(), evContent.getEditText(), "", "", evPrice.getEditText(), rbvStatus.getState(), evDate.getSelectText());
        dbHelper.insertOrReplace(money);
    }

    private void initEditData(){
        Intent intent=getIntent();
        if(intent!=null){
            money= intent.getParcelableExtra("Money");
            mPos= intent.getIntExtra("Pos",0);
            if(money!=null){
                setData();
            }
        }
    }

    private void setData(){
        evContent.setEditText(money.getContent());
        evPrice.setEditText(money.getPrice());
        evDate.setEditText(money.getDate());
        evAccount.setSelectText(money.getAccount());
        evClassify.setSelectText(money.getClassify());
        evAccount.setIcon(money.getAccountIcon(),resources);
        evClassify.setIcon(money.getClassifyIcon(),resources);
        evContent.setSelection(evContent.getEditText().length());
        evPrice.setSelection(evPrice.getEditText().length());
    }

    private void setEmpty(){



    }

    private void initDB() {
        SharedPreferences sp = getSharedPreferences("Config", MODE_PRIVATE);
        boolean isInited = sp.getBoolean("IsMoneyClasAccInited", false);
        LogUtil.i(TAG, "isInited=" + isInited);
        if (!isInited) {
            dbHelper.insertInitAccount();
            dbHelper.insertInitClassify();
            sp.edit().putBoolean("IsMoneyClasAccInited", true).apply();

            evAccount.setSelectText(DBHelper.accountDescriptions[0]);
            evAccount.setIcon(DBHelper.accountIcons[0], resources);

            evClassify.setSelectText(DBHelper.classifyDescriptions[0]);
            evClassify.setIcon(DBHelper.classifyIcons[0], resources);
        }

        evDate.setSelectText(DateUtil.getCurrentDate());

//        LogUtil.i(TAG, "icon_account=" + icon_account + ",icon_classify=" + icon_classify);
        if (iconClassify == null || iconAccount == null) {
            MoneyAccount moneyAccount = dbHelper.getAccountFirst();
            MoneyClassify moneyClassify = dbHelper.getClassifyFirst();

            iconAccount = moneyAccount.getIcon();
            evAccount.setSelectText(moneyAccount.getName());
            evAccount.setIcon(iconAccount, resources);

            iconClassify = moneyClassify.getIcon();
            evClassify.setSelectText(moneyClassify.getName());
            evClassify.setIcon(iconClassify, resources);
        }

        if (TextUtils.isEmpty(mType)) {
            LogUtil.i(TAG, "mType==NULL");
            setAccountData();
            setClassifyData();
        } else {
            LogUtil.i(TAG, "mType=" + mType);
            if (mType.equals("Classify")) {
                setClassifyData();
            } else if (mType.equals("Account")) {
                setAccountData();
            }
        }

        AppUtils.logListToString("分类", listsClassify);
        AppUtils.logListToString("账户", listsAccount);

    }

    private void setClassifyData() {
        listsClassify = AppUtils.initList(listsClassify);
        listsClassify = dbHelper.getMoneyClassifyLists(listsClassify);
        evClassify.setListAdapter(listsClassify);
    }

    private void setAccountData() {
        listsAccount = AppUtils.initList(listsAccount);
        listsAccount = dbHelper.getMoneyAccountLists(listsAccount);
        evAccount.setListAdapter(listsAccount);
    }

    private void setOnClick() {
        evClassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏软键盘
                AppUtils.closeSoftKb(MoneyEdit2Activity.this);
                evClassify.setTitle(getString(R.string.money_select_classify));
                evClassify.setTextAdd(getString(R.string.title_add_classify));
                evClassify.alertDialog();
                evClassify.setFabAddShow();
                evClassify.setListAdapter(listsClassify);

                evClassify.setOnItemMoveListener(new OnItemMoveListener() {
                    @Override
                    public void move(boolean finished) {
                        LogUtil.i(TAG, "isMoved:" + finished);
                        if (finished) {
                            dbHelper.deleteClassifyAll();
                            dbHelper.insertClassifyAll(listsClassify);
                        }
                    }

                    @Override
                    public void remove(boolean removed, String description) {
                        if (removed) {
                            dbHelper.deleteClassifyItemByKey(description);

                            String des = evClassify.getSelectText();
                            LogUtil.i(TAG, "current description is :" + des);

                            if (des.equals(description)) {
                                MoneyClassify entity = dbHelper.getClassifyFirst();
                                LogUtil.i(TAG, "when the first is deleted,current description is :" + entity.getName());
//                                mHandler.sendMessage(AppUtils.sendMsgToHandler(CLASSIFY_REFRESH, entity));
                            }
                        }
                    }
                });

                evClassify.setOnItemClickListener(new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ListDialog classify = listsClassify.get(position);
                        iconClassify = classify.icon;
                        evClassify.setSelectText(classify.name);
                        evClassify.setIcon(iconClassify, resources);
                        evClassify.dialogDismiss();
                    }
                });

                evClassify.setOnAddClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mType = "Classify";
                        Intent intent = new Intent(getApplicationContext(), AddListActivity.class);
                        intent.putExtra("Title", getString(R.string.title_add_classify));
                        intent.putExtra("Type", AddListActivity.TYPE_CLASSIFY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });
        evAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.closeSoftKb(MoneyEdit2Activity.this);
                evAccount.setTitle(getString(R.string.money_select_account));
                evAccount.setTextAdd(getString(R.string.title_add_accouunt));
                AppUtils.logListToString("点击了账户", listsAccount);
                evAccount.alertDialog();
                evAccount.setListAdapter(listsAccount);
                evAccount.setFabAddShow();
                evAccount.canMoveAndRemove(true);

                evAccount.setOnItemMoveListener(new OnItemMoveListener() {
                    @Override
                    public void move(boolean finished) {
                        if (finished) {
                            dbHelper.deleteAccountAll();
                            dbHelper.insertAccountAll(listsAccount);
                        }
                    }

                    @Override
                    public void remove(boolean removed, String description) {
                        if (removed) {
                            dbHelper.deleteAccountItemByKey(description);

                            String des = evAccount.getSelectText();
                            LogUtil.i(TAG, "current description is :" + des);

                            if (des.equals(description)) {
                                MoneyAccount entity = dbHelper.getAccountFirst();
                                LogUtil.i(TAG, "when the first is deleted,current description is :" + entity.getName());
//                                mHandler.sendMessage(AppUtils.sendMsgToHandler(ACCOUNT_REFRESH, entity));
                            }
                        }
                    }
                });

                evAccount.setOnItemClickListener(new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ListDialog account = listsAccount.get(position);
                        iconAccount = account.icon;
                        evAccount.setSelectText(account.name);
                        evAccount.setIcon(iconAccount, resources);
                        evAccount.dialogDismiss();
                    }
                });

                evAccount.setOnAddClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mType = "Account";
                        Intent intent = new Intent(getApplicationContext(), AddListActivity.class);
                        intent.putExtra("Title", getString(R.string.title_add_accouunt));
//                        intent.putExtra("StartIndex", START_INDEX_ACCOUNT);
//                        intent.putExtra("LastIndex", LAST_INDEX_ACCOUNT);
//                        intent.putExtra("PageSize", PAGE_SIZE);
                        intent.putExtra("Type", AddListActivity.TYPE_ACCOUNT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                });
            }
        });
        evDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                evDate.getSelectText();
            }
        });
        rbvStatus.setOnChangeListener();
    }

    /**
     * 日期选择器
     */
    private void datePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
                Date date1 = calendar.getTime();
                String strDate = sdf.format(date1);
                LogUtil.e(TAG, "strDate=" + strDate);

                evDate.setSelectText(strDate);
                evDate.setIcon(R.drawable.emoji_e_119);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void init() {
        evContent.setTvText(R.string.money_content);
        evClassify.setTvText(R.string.money_classify);
        evPrice.setTvText(R.string.money_price);
        evAccount.setTvText(R.string.money_account);
        rbvStatus.setTvText(R.string.money_state);
        evDate.setTvText(R.string.money_date);
        rbvStatus.setCheckedLeft(true);//默认支出
        rbvStatus.setLeftText(R.string.edit_money_out);//左边支出
        rbvStatus.setCenterText(R.string.edit_money_transfer);//中间转账
        rbvStatus.setRightText(R.string.edit_money_income);//右边收入
        evPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);
        evDate.setEditText(DateUtil.getCurrentDate());

        evClassify.setProperty(1);
        evAccount.setProperty(1);
        evDate.setProperty(1);

        evContent.setSingleLine();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }


}
