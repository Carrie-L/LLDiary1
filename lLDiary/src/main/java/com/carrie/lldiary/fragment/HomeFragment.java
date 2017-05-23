package com.carrie.lldiary.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.AnniversaryActivity;
import com.carrie.lldiary.activity.DiaryActivity;
import com.carrie.lldiary.activity.MoneyBookActivity;
import com.carrie.lldiary.activity.PlanActivity;
import com.carrie.lldiary.activity.StikyNoteActivity;
import com.carrie.lldiary.base.BaseFragment;
import com.carrie.lldiary.ui.DiaryListActivity;
import com.carrie.lldiary.ui.MoneyActivity;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HomeFragment extends BaseFragment implements OnClickListener {
    private ImageButton ib_my_diary, ib_my_plan, ib_my_stiky_note,
            ib_my_money_book, ib_my_anniversary, ib_my_robot;
    private Intent intent;
    private View view;
    private SharedPreferences sp;
    private TableLayout mTableLayout;
    @ViewInject(R.id.tv_home_my_diary)
    private TextView tv1;
    @ViewInject(R.id.tv_home_stiky_note)
    private TextView tv2;
    @ViewInject(R.id.tv_home_plan)
    private TextView tv3;
    @ViewInject(R.id.tv_home_anniversary)
    private TextView tv4;
    @ViewInject(R.id.tv_home_robot)
    private TextView tv5;
    @ViewInject(R.id.tv_home_money_book)
    private TextView tv6;
    @ViewInject(R.id.et_input_password)
    EditText et_input_password;
    @ViewInject(R.id.btn_confirm)
    Button btn_confirm;
    @ViewInject(R.id.btn_cancel)
    Button btn_cancel;
    @ViewInject(R.id.iv_bottom)
    private ImageView iv_botton_bg;
    private AlertDialog dialog;
    private static final int INPUT = 0;
    private static final int IMG = 1;
    protected static final int WHOLE = 1;
    protected static final int DIARY = 2;
    private static final String TAG = "HomeFragment";

    @Override
    public void initData(Bundle savedInstanceState) {
        ib_my_diary.setOnClickListener(this);
        ib_my_plan.setOnClickListener(this);
        ib_my_money_book.setOnClickListener(this);
        ib_my_stiky_note.setOnClickListener(this);
        ib_my_anniversary.setOnClickListener(this);
        ib_my_robot.setOnClickListener(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_home, null);
        ViewUtils.inject(this, view);
        iv_botton_bg.setVisibility(View.INVISIBLE);
        ib_my_diary = (ImageButton) view.findViewById(R.id.ib_home_my_diary);
        ib_my_plan = (ImageButton) view.findViewById(R.id.ib_home_plan);
        ib_my_stiky_note = (ImageButton) view
                .findViewById(R.id.ib_home_stiky_note);
        ib_my_money_book = (ImageButton) view
                .findViewById(R.id.ib_home_money_book);
        ib_my_anniversary = (ImageButton) view
                .findViewById(R.id.ib_home_anniversary);
        ib_my_robot = (ImageButton) view.findViewById(R.id.ib_home_robot);
        mTableLayout = (TableLayout) view.findViewById(R.id.layout);

        sp = mContext.getSharedPreferences("config", 0);
        int home_diary = sp.getInt("home_diary", 0);
        int home_note = sp.getInt("home_note", 0);
        int home_plan = sp.getInt("home_plan", 0);
        int home_ann = sp.getInt("home_ann", 0);
        int home_robot = sp.getInt("home_robot", 0);
        int home_money = sp.getInt("home_money", 0);
        int home_bg = sp.getInt("home_bg", 0);

        if (home_diary != 0 || home_note != 0 || home_ann != 0 || home_bg != 0
                || home_money != 0 || home_plan != 0 || home_robot != 0) {

            ib_my_diary.setImageResource(home_diary);
            ib_my_stiky_note.setImageResource(home_note);
            ib_my_plan.setImageResource(home_plan);
            ib_my_anniversary.setImageResource(home_ann);
            ib_my_robot.setImageResource(home_robot);
            ib_my_money_book.setImageResource(home_money);
            mTableLayout.setBackgroundResource(home_bg);
            if (sp.getInt("home_bg", 0) == R.drawable.home_bg3) {
                tv1.setTextColor(mContext.getResources()
                        .getColor(R.color.white));
                tv2.setTextColor(mContext.getResources()
                        .getColor(R.color.white));
                tv3.setTextColor(mContext.getResources()
                        .getColor(R.color.white));
                tv4.setTextColor(mContext.getResources()
                        .getColor(R.color.white));
                tv5.setTextColor(mContext.getResources()
                        .getColor(R.color.white));
                tv6.setTextColor(mContext.getResources()
                        .getColor(R.color.white));
            }
            if (sp.getInt("home_diary", 0) == R.drawable.home_diary2) {
                iv_botton_bg.setVisibility(View.VISIBLE);
            }
        } else {
            ib_my_diary.setImageResource(R.drawable.home_diary1);
            ib_my_stiky_note.setImageResource(R.drawable.home_note1);
            ib_my_plan.setImageResource(R.drawable.home_plan1);
            ib_my_anniversary.setImageResource(R.drawable.home_ann1);
            ib_my_robot.setImageResource(R.drawable.home_robot1);
            ib_my_money_book.setImageResource(R.drawable.home_money1);
            mTableLayout.setBackgroundResource(R.drawable.home_bg1);
        }

        calculateWeekMoney();

        return view;
    }

    private void calculateWeekMoney() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String currTime = DateUtil.getWeenAndTime();
                LogUtil.i(TAG, "currTime=" + currTime);//星期三 19:38:35

                String currTime2 = DateUtil.getCurrentTime2();
                LogUtil.i(TAG, "currTime2=" + currTime2);//2016-06-22 19:38:35

                if (currTime.contains("星期日")) {//23:59:00 星期日
                    LogUtil.i(TAG, "星期天计算周报");
                    String date = currTime2.split(" ")[0];
                    LogUtil.i(TAG, "date=" + date);
                    int year = Integer.valueOf(date.split("-")[0]);
                    int month = Integer.valueOf(date.split("-")[1]);
                    int day = Integer.valueOf(date.split("-")[2]);
                    LogUtil.i(TAG, "year=" + year + ",month=" + month + ",day=" + day);

                    String Monday = DateUtil.getLastWeekDate(year, month, day);
                    String Sunday = date;
                    LogUtil.i(TAG, "Monday=" + Monday + "-" + Sunday);

//					Cursor cursor= db.rawQuery("")

                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_home_my_diary:// 日记本
//			int pwdType=sp.getInt("pwdType", 2);
//			int cbPwd=sp.getInt("CbPwd", 0);
//			if(cbPwd==DIARY){//如果只加密日记本，判断是图片还是输入框密码锁
//				if(pwdType==IMG){
//					intent = new Intent(mContext, CheckImgPwdActivity.class);
//					startActivity(intent);
//				}
//				else if(pwdType==INPUT){
//					checkPassword();
//				}
//			}else{
//				Intent intent = new Intent(mContext,DiaryActivity.class);
//				startActivity(intent);
//			}
                Intent intent = new Intent(mContext, DiaryListActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_home_plan:// 计划表
                intent = new Intent(mContext, PlanActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_home_stiky_note:// 便签贴
                intent = new Intent(mContext, StikyNoteActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_home_money_book:// 记账本
                intent = new Intent(mContext, MoneyBookActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_home_anniversary:// 纪念日
                intent = new Intent(mContext, AnniversaryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.ib_home_robot:// 小恋机器人
//			intent = new Intent(mContext, RobotActivity.class);
                intent = new Intent(mContext, MoneyActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 验证密码是否正确
     */
    public void checkPassword() {
        View view = mInflater.inflate(R.layout.dialog_check_pwd, null);
        ViewUtils.inject(this, view);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_input_password.getText().toString().trim();//trim去两边的空格（中间的不去）
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                //判断密码是否正确 MD5Util.md5Password(password).equals(sp.getString("input_password", "")
                if (password.equals(sp.getString(Constant.SP_INPUT_PASSWORD, ""))) {
                    dialog.dismiss();
                    Toast.makeText(mContext, "密码正确", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, DiaryActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "密码错误", Toast.LENGTH_SHORT).show();
                    et_input_password.setText("");
                }
            }
        });
        builder.setView(view);
        dialog = builder.show();
    }

    @Override
    public void onPause() {
        super.onPause();

        LogUtil.i(TAG, "----------------onPause()-------------------------");
    }

    @Override
    public void onResume() {
        super.onResume();

        LogUtil.i(TAG, "----------------onResume()-------------------------");
    }

    @Override
    public void onStop() {
        super.onStop();

        LogUtil.i(TAG, "----------------onStop()-------------------------");
    }

    @Override
    public void onStart() {
        super.onStart();

        LogUtil.i(TAG, "----------------onStart()-------------------------");
    }


}
