package com.carrie.lldiary.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.base.MyBaseActivity;
import com.carrie.lldiary.dao.Plan;
import com.carrie.lldiary.db.PlanDB;
import com.carrie.lldiary.entity.ListDialog;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.utils.AlarmUtil;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.EditView;
import com.carrie.lldiary.view.RadioButtonView;
import com.carrie.lldiary.view.TitleView;
import com.lidroid.xutils.ViewUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.Calendar;

public class PlanEditActivity extends MyBaseActivity implements OnClickListener {
    private static final String TAG = "PlanEditActivity";
    private EditText et_plan_content;
    private RadioButtonView rg_finish, rg_remind;
    private ImageButton ib_back, ib_save;
    private EditView editView;

    private Plan entity;
    private ArrayList<Plan> plans;
    private RadioButton rb_complish;
    private int complishId;
    private int remindId;
    private RadioButton rb_remind;
    private PlanDB mPlanDB;
    private Intent intent;
    private int position;
    private Long id;
    private String isComplish;
    private String isRemind;

    private Context mContext;


    private String mTime;
    private int mHour;
    private int mMinute;
    private String content, startTime, endTime, date, importantDegree;
    private int icon;

    private SharedPreferences sp;
    private long lTime;
    private ContentValues cv;
    private TextView tv_startTime;
    private TextView tv_endTime;
    private TextView tv_date;
    private boolean finish, remind;
    private long timeInMillis;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_edit_plan, container, false);
        MyActivityManager.add(this);
        ViewUtils.inject(this);
        PushAgent.getInstance(this).onAppStart();// 统计应用启动数据
        setTitleView();
        mContext=getApplicationContext();

        et_plan_content = (MaterialEditText) view.findViewById(R.id.et_plan_content);
        rg_finish = (RadioButtonView) view.findViewById(R.id.rg_plan_finish);
        rg_remind = (RadioButtonView) view.findViewById(R.id.rg_plan_remind);
        tv_startTime = (TextView) view.findViewById(R.id.tv_plan_time1);
        tv_endTime = (TextView) view.findViewById(R.id.tv_plan_time2);
        tv_date = (TextView) view.findViewById(R.id.tv_remind_date);
        editView = (EditView) view.findViewById(R.id.plan_icon_view);

        editView.setTvText(getString(R.string.plan_id));
        editView.setProperty(1);

        return view;
    }

    private void setTitleView() {
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        titleView.setTitle(getString(R.string.title_activity_plan_edit));
        titleView.setOnRightClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editPlan();
            }
        });
        titleView.setOnLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PlanActivity.class);
                startActivity(intent);
                finish();
            }
        });
        titleView.setRightIcon(R.drawable.btn_ok);
    }

    @Override
    public void initData() {
        sp = getSharedPreferences("config", 0);
        Intent intent = getIntent();
        entity = (Plan) intent.getSerializableExtra("Plan");
        LogUtil.i(TAG, "entity=" + entity);

        initDate();
        setRBText();

        if (entity != null) {
            getIntentData();
            setData();
        } else {
            id = null;
            date = DateUtil.date2();
            LogUtil.i(TAG, "date1111=" + date);
            DateUtil.fixDate(date);
            tv_date.setText(date);
            importantDegree = getString(R.string.edit_plan_import1);
            icon = R.drawable.plan_priority_1;
            editView.setIcon(icon);
            editView.setSelectText(importantDegree);
            rg_remind.setCheckedLeft(true);
            rg_finish.setCheckedLeft(true);
        }

        setOnClick();

        tv_startTime.setOnClickListener(this);
        tv_endTime.setOnClickListener(this);
        tv_date.setOnClickListener(this);
    }

    private void initDate() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void setRBText() {
        rg_finish.setTvText(getString(R.string.edit_plan_finish_state));
        rg_finish.setLeftText(R.string.edit_plan_unFinish);
        rg_finish.setRightText(R.string.edit_plan_finish);

        rg_remind.setTvText(getString(R.string.ann_isRemind));
        rg_remind.setLeftText(R.string.ann_unremind);
        rg_remind.setRightText(R.string.ann_remind);


    }

    private void getIntentData() {
        content = entity.getContent();
        startTime = entity.getStartTime();
        endTime = entity.getEndTime();
        date = entity.getDate();
        finish = entity.getFinish();
        remind = entity.getRemind();
        id = entity.getId();
        importantDegree = entity.getImportantDegree();
        icon = entity.getIcon();

    }

    private void setData() {
        et_plan_content.setText(content);
        tv_startTime.setText(startTime);
        tv_endTime.setText(endTime);
        tv_date.setText(date);
        editView.setSelectText(importantDegree);
        editView.setIcon(icon);

        if (finish) {
            rg_finish.setCheckedRight(finish);
        }
        if (remind) {
            rg_remind.setCheckedRight(remind);
        }
    }

    private ArrayList<ListDialog> icons;


    private void setOnClick() {
        icons = AppUtils.initList(icons);
        icons = ListDialog.fillPlanIcons(getApplicationContext(), icons);

        editView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editView.setTitle(mContext.getResources().getString(R.string.plan_select_priority));
                editView.alertDialog();
                editView.setListAdapter(icons);
                editView.canMoveAndRemove(false);

                editView.setOnItemClickListener(new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ListDialog entity = icons.get(position);
                        editView.setSelectText(entity.name);
                        editView.setIcon(entity.icon,getResources());
                        editView.dialogDismiss();
                    }
                });
            }
        });
        setCheckedChaneListener(rg_finish);
        setCheckedChaneListener(rg_remind);

    }

    private void setCheckedChaneListener(final RadioButtonView rgv) {
        rgv.setOnChangeListenerLeft(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rgv.setCheckedLeft(true);
                }
            }
        });
        rgv.setOnChangeListenerRight(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rgv.setCheckedRight(true);
                }
            }
        });
    }

    public void editPlan() {
        content = et_plan_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "计划内容不能为空哦~", Toast.LENGTH_SHORT).show();
            return;
        }
        importantDegree = editView.getSelectText();
        icon = editView.getIcon();
        startTime = tv_startTime.getText().toString().trim();
        endTime = tv_endTime.getText().toString().trim();
        date = tv_date.getText().toString().trim();

        if (endTime.compareTo(startTime) < 0) {
            Toast.makeText(getApplicationContext(), "结束时间不能小于开始时间", Toast.LENGTH_SHORT).show();
            return;
        }

        int state_finish = rg_finish.getState();//1,左边；0，右边
        int state_remind = rg_remind.getState();//1,左边；0，右边

        entity = new Plan(id, importantDegree, icon, content, state_remind == 0 ? true : false, state_finish == 0 ? true : false, date, startTime, endTime);
        LogUtil.i(TAG, "PLAN=" + entity.toString());

        App.dbHelper.insertPlanItem(entity);
        if (id == null) {
            LogUtil.i(TAG, "插入");
//			PlanActivity.entities.add(entity);
        } else {
            LogUtil.i(TAG, "更新");
        }

        if (remind) {
            AlarmUtil.setAlarm(getApplicationContext(), DateUtil.string2Long2(startTime), content);
        }
        goBack();
        // 自定义事件次数统计
        StatService.onEvent(this, "editPlan", "编辑计划表", 1);
        // 自定义事件时长统计
        StatService.onEventEnd(this, "editPlan", "编辑计划表");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_remind_date:
                datePicker();
                break;
            case R.id.tv_plan_time1:
                timePickerDialog(tv_startTime);
                break;
            case R.id.tv_plan_time2:
                timePickerDialog(tv_endTime);
                break;
            default:
                break;
        }
    }

    /**
     * 时间选择对话框
     */
    public void timePickerDialog(final TextView textView) {
        Editor editor = sp.edit();
        // editor.putBoolean("isPickTime", true).commit();//是否设置了提醒时间
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(this,
                new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        timeInMillis = calendar.getTimeInMillis();
                        LogUtil.i(TAG, "calendar.getTimeInMillis()=" + timeInMillis);
                        mTime = format(hourOfDay) + ":" + format(minute);
                        System.out.println("mTime=" + mTime);

                        if ((textView.equals(tv_endTime)) && !TextUtils.isEmpty(tv_startTime.getText().toString())) {
                            int compare = mTime.compareTo(tv_startTime.getText().toString());
                            LogUtil.i(TAG, "compare=" + compare);
                            if (compare < 0) {
                                Toast.makeText(getApplicationContext(), "结束时间不能小于开始时间", Toast.LENGTH_SHORT).show();
                            }
                        }
                        textView.setText(mTime);
                    }
                }, mHour, mMinute, true);
        dialog.show();
    }

    private Calendar calendar;
    private int year;
    private int month;
    private int day;

    /**
     * 日期选择器
     */
    private void datePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //月份+1
                monthOfYear = monthOfYear + 1;
                String date = year + "年" + (monthOfYear < 10 ? "0" + monthOfYear : monthOfYear) + "月" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "日";
                String week = DateUtil.getWeekAccordingDate(date, calendar);

                date = date + " " + week;
                tv_date.setText(date);
                LogUtil.e(TAG, "date=" + date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * 格式化字符串(7:3->07:03)
     */
    private String format(int x) {
        String s = "" + x;
        if (s.length() == 1)
            s = "0" + s;
        return s;
    }

    // 友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        StatService.onPageStart(this, "计划表编辑");
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        StatService.onPageEnd(this, "计划表编辑");
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode==KeyEvent.KEYCODE_BACK){
//            Intent intent=new Intent(getApplicationContext(),PlanActivity.class);
//            startActivity(intent);
//            finish();
//            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void goBack() {
        Intent intent=new Intent(getApplicationContext(),PlanActivity.class);
        startActivity(intent);
        finish();
    }
}
