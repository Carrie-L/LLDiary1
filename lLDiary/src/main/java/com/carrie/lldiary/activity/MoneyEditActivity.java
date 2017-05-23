package com.carrie.lldiary.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.base.MyBaseActivity;
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
import com.carrie.lldiary.view.EditView;
import com.carrie.lldiary.view.RadioButtonView;
import com.carrie.lldiary.view.TitleView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MoneyEditActivity extends MyBaseActivity implements
        OnCheckedChangeListener {
    private static final String TAG = "MoneyEditActivity";

    //图标对话框长度在view_dialog_icon.xml修改
    //Account图标
    private static final int START_INDEX_ACCOUNT = 305;
    private static final int LAST_INDEX_ACCOUNT = 313;

    private static final int PAGE_SIZE = 70;

    //Classify图标
    private static final int START_INDEX_CLASSIFY = 12;
    private static final int LAST_INDEX_CLASSIFY = 277;

    private EditView ev_content;
    private EditView ev_account;
    private EditView ev_price;
    private EditView ev_classify;
    private RadioButtonView rbv_state;
    private EditView ev_date;

    private ArrayList<Money> entities;
    /**-1 支出； 0 转账 ；  1 收入*/
    private int state;
    private String icon_classify="";
    private String icon_account="";
    private String content;
    private String account;
    private String classify;
    private String price;
    private String date;
    private Long id;

    private ArrayList<ListDialog> listsClassify;
    private ArrayList<ListDialog> listsAccount;

    private Calendar calendar;
    private int year;
    private int month;
    private int day;

    private String mType;
    private DBHelper dbHelper;
    private static final int ACCOUNT_REFRESH = 0;
    private static final int CLASSIFY_REFRESH = 1;

    private String originalPrice="0";
    /**差价**/
    private float priceDifferences=0;
    private Resources resources;


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_edit_money, container,false);
        ev_content = (EditView) view.findViewById(R.id.et_money_content);
        ev_account = (EditView) view.findViewById(R.id.et_money_account);
        ev_classify = (EditView) view.findViewById(R.id.et_money_classify);
        ev_price = (EditView) view.findViewById(R.id.et_money_price);
        rbv_state = (RadioButtonView) view.findViewById(R.id.rb_money_state);
        ev_date = (EditView) view.findViewById(R.id.et_money_date);

        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        titleView.setTitle("编辑记账本");
        titleView.setOnRightClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editMoneyBook();
            }
        });
        titleView.setOnLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                boolean isNotEmpty = !TextUtils.isEmpty(ev_content.getString()) || !TextUtils.isEmpty(ev_price.getString());
                boolean isChanged=ev_account.isEdited||ev_price.isEdited;
                if (!AppUtils.backToast(MoneyEditActivity.this, isChanged)) {
                    finish();
                }
            }
        });
        titleView.setRightIcon(R.drawable.btn_ok);
        return view;
    }

    @Override
    public void initData() {
        MyActivityManager.add(this);
        PushAgent.getInstance(this).onAppStart();// 统计应用启动数据
        resources = getResources();
        init();

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        LogUtil.i(TAG, "position=" + position);

        if (position >= 0) {
            entities = (ArrayList<Money>) intent.getSerializableExtra("MoneyList");

            getIntentDatas(position);
            setIntentData();
        } else {
            entities = MoneyBookActivity.entities;
            date = DateUtil.getDateWeek();
            ev_date.setSelectText(date);
            ev_date.setIcon(R.drawable.emoji_e_119);
            rbv_state.setCheckedCenter(true);//默认支出
            LogUtil.d(TAG, "date=" + date);
        }
        setOnClick();
    }

    private void init() {
        ev_content.setTvText(R.string.money_content);
        ev_classify.setTvText(R.string.money_classify);
        ev_price.setTvText(R.string.money_price);
        ev_account.setTvText(R.string.money_account);
        rbv_state.setTvText(R.string.money_state);
        ev_date.setTvText(R.string.money_date);
        rbv_state.setLeftText(R.string.edit_money_income);//左边收入
        rbv_state.setCenterText(R.string.edit_money_out);//中间支出
        rbv_state.setRightText(R.string.edit_money_transfer);//右边转账
        ev_price.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);

        ev_classify.setProperty(1);
        ev_account.setProperty(1);
        ev_date.setProperty(1);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


    }


    private void getIntentDatas(int position) {
        Money moneyEntity = entities.get(position);
        content = moneyEntity.getContent();
        price = moneyEntity.getPrice();
        classify = moneyEntity.getClassify();
        account = moneyEntity.getAccount();
        state = moneyEntity.getState();
        date = moneyEntity.getDate();
        icon_classify = moneyEntity.getClassifyIcon();
        icon_account = moneyEntity.getAccountIcon();
        id = moneyEntity.getId();
        originalPrice=price;
    }

    private void setIntentData() {
        ev_content.setEditText(content);
        ev_price.setEditText(price);
        ev_classify.setSelectText(classify);
        ev_classify.setIcon(icon_classify,resources);
        ev_account.setSelectText(account);
        ev_account.setIcon(icon_account,resources);
        ev_date.setSelectText(date);
        ev_date.setIcon(R.drawable.emoji_e_119);

        if (state == -1) {
            rbv_state.setCheckedCenter(true);//支出
        } else if (state ==1){
            rbv_state.setCheckedLeft(true);//收入
        }else if (state ==0){
            rbv_state.setCheckedRight(true);//转账
        }

        ev_content.setSelection(content.length());
        ev_price.setSelection(price.length());
    }

    private void initDB() {
        LogUtil.i(TAG, "initDB()");

        dbHelper = App.dbHelper;

        SharedPreferences sp = getSharedPreferences("Config", MODE_PRIVATE);
        boolean isInited = sp.getBoolean("IsMoneyClasAccInited", false);
        LogUtil.i(TAG, "isInited=" + isInited);
        if (!isInited) {
            dbHelper.insertInitAccount();
            dbHelper.insertInitClassify();
            sp.edit().putBoolean("IsMoneyClasAccInited", true).apply();

            ev_account.setSelectText(DBHelper.accountDescriptions[0]);
            ev_account.setIcon(DBHelper.accountIcons[0],resources);

            ev_classify.setSelectText(DBHelper.classifyDescriptions[0]);
            ev_classify.setIcon(DBHelper.classifyIcons[0],resources);
        }

        LogUtil.i(TAG, "icon_account=" + icon_account + ",icon_classify=" + icon_classify);
        if (TextUtils.isEmpty(icon_account)||TextUtils.isEmpty(icon_classify)||icon_account .equals("0") || icon_classify.equals("0")) {
            MoneyAccount moneyAccount = dbHelper.getAccountFirst();
            MoneyClassify moneyClassify = dbHelper.getClassifyFirst();

            ev_account.setSelectText(moneyAccount.getName());
            ev_account.setIcon(moneyAccount.getIcon(),resources);

            ev_classify.setSelectText(moneyClassify.getName());
            ev_classify.setIcon(moneyClassify.getIcon(),resources);
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
        ev_classify.setListAdapter(listsClassify);
    }

    private void setAccountData() {
        listsAccount = AppUtils.initList(listsAccount);
        listsAccount = dbHelper.getMoneyAccountLists(listsAccount);
        ev_account.setListAdapter(listsAccount);
    }

    private void setOnClick() {
        ev_classify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏软键盘
                AppUtils.closeSoftKb(MoneyEditActivity.this);
                ev_classify.setTitle(getString(R.string.money_select_classify));
                ev_classify.setTextAdd(getString(R.string.title_add_classify));
                ev_classify.alertDialog();
                ev_classify.setListAdapter(listsClassify);

                ev_classify.setOnItemMoveListener(new OnItemMoveListener() {
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

                            String des = ev_classify.getSelectText();
                            LogUtil.i(TAG, "current description is :" + des);

                            if (des.equals(description)) {
                                MoneyClassify entity = dbHelper.getClassifyFirst();
                                LogUtil.i(TAG, "when the first is deleted,current description is :" + entity.getName());
                                mHandler.sendMessage(AppUtils.sendMsgToHandler(CLASSIFY_REFRESH, entity));
                            }
                        }
                    }
                });

                ev_classify.setOnItemClickListener(new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ListDialog classify = listsClassify.get(position);
                        ev_classify.setSelectText(classify.name);
                        ev_classify.setIcon(classify.icon,resources);
                        ev_classify.dialogDismiss();
                    }
                });

                ev_classify.setOnAddClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mType = "Classify";

                        Intent intent = new Intent(getApplicationContext(), EditListActivity.class);
                        intent.putExtra("Title", getString(R.string.title_add_classify));
                        intent.putExtra("StartIndex", START_INDEX_CLASSIFY);
                        intent.putExtra("LastIndex", LAST_INDEX_CLASSIFY);
                        intent.putExtra("PageSize", PAGE_SIZE);
                        intent.putExtra("Type", mType);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });
        ev_account.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.closeSoftKb(MoneyEditActivity.this);
                ev_account.setTitle(getString(R.string.money_select_account));
                ev_account.setTextAdd(getString(R.string.title_add_accouunt));
                AppUtils.logListToString("点击了账户", listsAccount);
                ev_account.alertDialog();
                ev_account.setListAdapter(listsAccount);
                ev_account.setFabAddShow();
                ev_account.canMoveAndRemove(true);

                ev_account.setOnItemMoveListener(new OnItemMoveListener() {
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

                            String des = ev_account.getSelectText();
                            LogUtil.i(TAG, "current description is :" + des);

                            if (des.equals(description)) {
                                MoneyAccount entity = dbHelper.getAccountFirst();
                                LogUtil.i(TAG, "when the first is deleted,current description is :" + entity.getName());
                                mHandler.sendMessage(AppUtils.sendMsgToHandler(ACCOUNT_REFRESH, entity));
                            }
                        }
                    }
                });

                ev_account.setOnItemClickListener(new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ListDialog account = listsAccount.get(position);
                        ev_account.setSelectText(account.name);
                        ev_account.setIcon(account.icon,resources);
                        ev_account.dialogDismiss();
                    }
                });

                ev_account.setOnAddClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mType = "Account";
                        Intent intent = new Intent(getApplicationContext(), EditListActivity.class);
                        intent.putExtra("Title", getString(R.string.title_add_accouunt));
                        intent.putExtra("StartIndex", START_INDEX_ACCOUNT);
                        intent.putExtra("LastIndex", LAST_INDEX_ACCOUNT);
                        intent.putExtra("PageSize", PAGE_SIZE);
                        intent.putExtra("Type", mType);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                });
            }
        });
        ev_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                ev_date.getSelectText();
            }
        });
        rbv_state.setOnChangeListenerLeft(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbv_state.setCheckedLeft(isChecked);
                }
            }
        });

        rbv_state.setOnChangeListenerCenter(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbv_state.setCheckedCenter(isChecked);
                }
            }
        });

        rbv_state.setOnChangeListenerRight(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbv_state.setCheckedRight(isChecked);
                }
            }
        });
    }

    /**
     * 日期选择器
     */
    private void datePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //月份+1
                monthOfYear = monthOfYear + 1;
                String date = year + "年" + monthOfYear + "月" + dayOfMonth + "日";
                String week = DateUtil.getWeekAccordingDate(date, calendar);
                date = date + " " + week;
                ev_date.setSelectText(date);
                ev_date.setIcon(R.drawable.emoji_e_119);

                LogUtil.e(TAG, "date=" + date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    String income="";
    String expense="";
    public void editMoneyBook() {
        getData();
        if(!checkContentEmpty()){
            return;
        }

        ev_content.setSelection(content.length());
        ev_price.setSelection(price.length());
        DecimalFormat decimal = new DecimalFormat("#.##");
        price = decimal.format(Double.valueOf(price));

        income=state == 1 ? price : "0";
        expense=state == -1 ? price : "0";

        rechargeToAccount();

        Money money = new Money(id, icon_classify, icon_account, classify, account, content,income ,expense,price , state, date);
        if (id != null) {//更新
            dbHelper.updateMoney(money);
        } else {//添加
            dbHelper.insertMoney(money);
            entities.add(money);
        }

        AppUtils.logArrayListToString(entities);

        Intent intent = new Intent(this, MoneyBookActivity.class);
        intent.putExtra("MoneyList", entities);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

        AppUtils.StatEvent(this, "editMoney", "编辑记账本");
    }

    private void rechargeToAccount(){
        if(!dbHelper.isAccountUnique(content)){

            if(!price.equals(originalPrice)){
                //充值，从一个账户(from,选择的账户)的钱转到另一个账户(to,输入的账户)
                MoneyAccount fromAccount=dbHelper.getAccountItem(account);
                MoneyAccount toAccount=dbHelper.getAccountItem(content);
                String fromBalance=fromAccount.getBalance();
                String toBalance=toAccount.getBalance();

                priceDifferences=Float.valueOf(price)-Float.valueOf(originalPrice);
                LogUtil.d(TAG,"price="+price+",originalPrice="+originalPrice+",差价="+priceDifferences);

                if(TextUtils.isEmpty(fromBalance)){
                    fromBalance="0";
                }
                if(TextUtils.isEmpty(toBalance)){
                    toBalance="0";
                }

                float fromBalance1=Float.valueOf(fromBalance)-priceDifferences;
                LogUtil.i(TAG,"from account 剩余："+fromBalance1);
                float toBalance1=Float.valueOf(toBalance)+priceDifferences;
                LogUtil.i(TAG,"to account 剩余："+toBalance1);
                fromBalance=String.valueOf(fromBalance1);
                toBalance=String.valueOf(toBalance1);
                LogUtil.i(TAG,"fromBalance="+fromBalance+",toBalance="+toBalance);

                fromAccount.setBalance(fromBalance);
                dbHelper.updateMoneyAccount(fromAccount);

                toAccount.setBalance(toBalance);
                dbHelper.updateMoneyAccount(toAccount);


            }
            //因为是转账，把钱从一个地方存到另一个地方，所以不算收入也不算支出
            income="0";
            expense="0";
        }
    }

    private void getData() {
        content = ev_content.getString();
        price = ev_price.getString();
        classify = ev_classify.getSelectText();
        account = ev_account.getSelectText();
        state = rbv_state.getState();
        date = ev_date.getSelectText();
        icon_classify = ev_classify.getIconName();
        icon_account = ev_account.getIconName();
    }

    private boolean checkContentEmpty() {
        if (TextUtils.isEmpty(content)) {
            AppUtils.toast00(getApplicationContext(), R.string.toast_money_content);
            return false;
        }
        if (TextUtils.isEmpty(price)) {
            AppUtils.toast00(getApplicationContext(), R.string.toast_money_price);
            return false;
        }
        if (TextUtils.isEmpty(classify)) {
            AppUtils.toast00(getApplicationContext(), R.string.toast_money_classify);
            return false;
        }
        if (TextUtils.isEmpty(account)) {
            AppUtils.toast00(getApplicationContext(), R.string.toast_money_account);
            return false;
        }

        if (icon_classify .equals("0")) {
            icon_classify = dbHelper.queryClassifyIcon(classify);
        }

        if (icon_account.equals("0")) {
            icon_account = dbHelper.queryAccountIcon(account);
        }

        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
    }

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ACCOUNT_REFRESH:
                    MoneyAccount account = (MoneyAccount) msg.obj;
                    ev_account.setSelectText(account.getName());
                    ev_account.setIcon(account.getIcon(),resources);
                    LogUtil.i(TAG, "refresh: current account description is :" + account.getName());
                    break;

                case CLASSIFY_REFRESH:
                    MoneyClassify classify = (MoneyClassify) msg.obj;
                    ev_classify.setSelectText(classify.getName());
                    ev_classify.setIcon(classify.getIcon(),resources);
                    LogUtil.i(TAG, "refresh: current account description is :" + classify.getName());
                    break;
            }

        }
    };

    // 友盟统计
    public void onResume() {
        super.onResume();
        LogUtil.i(TAG, "onResume");
        MobclickAgent.onResume(this);
        StatService.onPageStart(this, "记账本编辑");

        initDB();
    }

    public void onPause() {
        super.onPause();
        LogUtil.i(TAG, "onPause");
        MobclickAgent.onPause(this);
        StatService.onPageEnd(this, "记账本编辑");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean isChanged=ev_account.isEdited||ev_price.isEdited;
            if (!AppUtils.backToast(MoneyEditActivity.this, isChanged)) {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void goBack() {

    }
}
