package com.carrie.lldiary.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.base.BaseActivity;
import com.carrie.lldiary.dao.Ann;
import com.carrie.lldiary.helper.DBHelper;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.DialogIconView;
import com.carrie.lldiary.view.EditTextView;
import com.carrie.lldiary.view.EmojiconView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnniversaryEditActivity extends BaseActivity {
    private static final String TAG = "AnniversaryEditActivity";
    @BindView(R.id.etv_ann_content)
    protected EditTextView etv_content;
    @BindView(R.id.etv_ann_person)
    protected EditTextView etv_person;
    @BindView(R.id.etv_ann_relationship)
    protected EditTextView etvRelationShip;
    @BindView(R.id.etv_ann_date)
    protected EditTextView etv_date;
    @BindView(R.id.etv_ann_remark)
    protected EditTextView etv_remark;
    @BindView(R.id.switch_calendar)
    protected Switch aSwitchCalendar;
    private Integer mPos=0;

    String content, person, date, time, relationship,remark;
    String icon;
    int iconId;
    Long id;
    private Ann entity;
    private boolean isCalRemind=true;
    private DBHelper dbHelper;
    private Resources resources;

    @Override
    protected void initPre() {
        mLayoutId = R.layout.activity_edit_anniversarye;
        mTitle = getString(R.string.title_activity_anniversarye_edit);
        mRightIcon = R.drawable.btn_ok;
        titleView.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAnn();
            }
        });
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
        dbHelper = App.dbHelper;
        entity=new Ann();
        Intent intent=getIntent();
        if(intent!=null){
            entity=intent.getParcelableExtra("Ann");
            mPos=intent.getIntExtra("pos",0);
        }
        id = null;
        resources = getResources();
        icon="emoji_e_262";
        iconId=AppUtils.getResourceIdentifier(resources,icon,"drawable");
        LogUtil.i(TAG,"iconId="+iconId);
        date = DateUtil.getCurrentDate();
        time = DateUtil.getCurrentTime();

        initTextView();
        getIntentData();
        setData();

        aSwitchCalendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCalRemind = isChecked;
            }
        });
    }

    private void initTextView() {
        etv_content.setText(getString(R.string.ann_content));
        etv_person.setText(getString(R.string.ann_person));
        etvRelationShip.setText(getString(R.string.ann_relationship));
        etv_date.setText(getString(R.string.my_anniversary));
        etv_remark.setText(getString(R.string.ann_remark));

        etv_content.setFocusMode(true);
        etv_person.setFocusMode(true);
        etvRelationShip.setFocusMode(true);
        etv_remark.setFocusMode(true);
        etv_date.setFocusMode(false);
        etv_date.setFocusModeRight(false);
        etv_date.setRightVisible();

        etv_content.setHintText(R.string.hint_input);
        etv_person.setHintText(R.string.hint_input);
        etvRelationShip.setHintText(R.string.hint_input);
        etv_date.setHintText(R.string.hint_click);
        etv_date.setHintTextRight(R.string.hint_click);

        etv_content.showClearButton(true);
        etv_person.showClearButton(true);
        etvRelationShip.showClearButton(true);
        etv_remark.showClearButton(true);

        etv_content.setFloatLabelText(getString(R.string.ann_float_content));
        etv_date.setFloatLabelText(getString(R.string.ann_date));
        etv_date.setFloatLabelTextRight(getString(R.string.ann_time));
        etv_person.setFloatLabelText(getString(R.string.ann_person_hint));
        etvRelationShip.setFloatLabelText(getString(R.string.ann_relationship_hint));
        etv_remark.setFloatLabelText(getString(R.string.ann_relationship_remark));

        etv_content.setFloatingLabelAlwaysShown(true);
        etv_person.setFloatingLabelAlwaysShown(true);
        etvRelationShip.setFloatingLabelAlwaysShown(true);
        etv_date.setFloatingLabelAlwaysShown(true);
        etv_date.setFloatingLabelAlwaysShownRight(true);
        etv_remark.setFloatingLabelAlwaysShown(true);

        etv_content.setFloatLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etv_person.setFloatLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etvRelationShip.setFloatLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etv_date.setFloatLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etv_date.setFloatLabelRight(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etv_remark.setFloatLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
    }

    private void getIntentData() {
        if (entity != null) {
            content = entity.getContent();
            person = entity.getPerson();
            relationship = entity.getRelationship();
            remark=entity.getRemark();
            date = entity.getDate().split(" ")[0];
            time = entity.getDate().split(" ")[1];
            LogUtil.i(TAG, "getIntentData: date=" + date + ",time=" + time);
            icon = entity.getIcon();
            id = entity.getId();
            isCalRemind = entity.getRemind().equals("1");

            iconId=AppUtils.getResourceIdentifier(resources,icon,"drawable");
        }
    }

    private void setData() {
        etv_content.setIconRight(iconId );
        etv_content.setEditText(content);
        etv_person.setEditText(person);
        etvRelationShip.setEditText(relationship);
        etv_remark.setEditText(remark);
        if (!TextUtils.isEmpty(content)) {
            etv_content.setSelection(content.length());
        }
        if (!TextUtils.isEmpty(person)) {
            etv_person.setSelection(person.length());
        }
        if (!TextUtils.isEmpty(relationship)) {
            etvRelationShip.setSelection(relationship.length());
        }

        etv_date.alertDateDialog();
        etv_date.alertTimeDialog();
        etv_content.setLongPress(true);
        etv_date.setEditText(date);
        etv_date.setEditTextRight(time);

        final DialogIconView iconView = new DialogIconView(getApplicationContext());
        etv_content.setIconDialog(iconView, 259, 304, 50);
        iconView.setOnDialogItemClickListener(new EmojiconView.OnDialogItemClickListener() {
            @Override
            public void selectIcon(int resId) {
                ((ViewGroup) iconView.getParent()).removeView(iconView);
                iconId = resId;
                etv_content.setIconRight(resId);
                icon=resources.getResourceName(resId);
                LogUtil.i(TAG, "1 icon=" + icon);
            }
        });
        aSwitchCalendar.setChecked(isCalRemind);
    }

    private void editAnn() {
        content = etv_content.getEditText();
        person = etv_person.getEditText();
        relationship = etvRelationShip.getEditText();
        date = etv_date.getEditText();
        time = etv_date.getEditTextRight();
        isCalRemind=aSwitchCalendar.isChecked();
        remark=etv_remark.getEditText();

        LogUtil.i(TAG, "2 date=" + date);
        LogUtil.i(TAG, "2 time=" + time);
        LogUtil.i(TAG, "2 isCalRemind=" + isCalRemind);
        LogUtil.e(TAG, "2 relationship=" + relationship);

        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_ann_content), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(person)) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_ann_person), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(relationship)) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_ann_relationship), Toast.LENGTH_SHORT).show();
            return;
        }
        save();
    }

    private void save() {
        entity = new Ann(id, content, person, relationship, icon, date + " " + time, isCalRemind?"1":"0",remark);
        LogUtil.i(TAG, "ann===" + entity.toString());
        if (id == null) {
            id = dbHelper.insertAnn(entity);
        } else {
            dbHelper.updateAnn(entity);
        }
        entity.durTime= DateUtil.getDurTime(entity.getDate());
        Toast.makeText(this, "save ann success: " + id, Toast.LENGTH_SHORT).show();
        if (aSwitchCalendar.isChecked()) {
            addToCalendar();
        }
        Intent intent=new Intent();
        intent.putExtra("Ann",entity);
        intent.putExtra("pos",mPos);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void addToCalendar() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date time;
        try {
            time = sdFormat.parse(entity.getDate());
            calendar.setTime(time);
            int year = calendar.get(Calendar.YEAR);
            int monthOfYear = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            LogUtil.i(TAG, "year=" + year + ",monthOfYear=" + monthOfYear + ",dayOfMonth=" + dayOfMonth + ",hour=" + hour + ",minute=" + minute);

            calendar.set(year, monthOfYear, dayOfMonth, hour, minute);//添加到日历时，month-1,而上面的monthOfYear比输出值小1，所以刚好monthOfYear不用减，set方法也不用+1
            long startMills = calendar.getTimeInMillis();
            calendar.set(year, monthOfYear, dayOfMonth, 23, 59);
            long endMills = calendar.getTimeInMillis();
            LogUtil.i(TAG, "startMills=" + startMills + ",endMills=" + endMills);

            boolean isAddCalSuccess = DateUtil.addToCalendar(this, entity.getId(), entity.getContent(), entity.getPerson() + " ^_^ " + entity.getRelationship(), "", startMills, endMills, false);
            LogUtil.i(TAG, "save to cal : " + isAddCalSuccess);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void notification(){

    }

    private boolean checkUpdate(){
//        if(!etv_content.getEditText().equals(entity.getContent())){
//            LogUtil.i(TAG,"etv_content.getEditText()="+etv_content.getEditText()+",,,entity.getContent()="+entity.getContent());
//        }
//        if(!etv_person.getEditText().equals(entity.getPerson())){
//            LogUtil.i(TAG,"etv_person.getEditText()="+etv_person.getEditText()+",,,entity.getPerson()="+entity.getPerson());
//        }
//        if(!etvRelationShip.getEditText().equals(entity.getRelationship())){
//            LogUtil.i(TAG,"etvRelationShip.getEditText()="+etvRelationShip.getEditText()+",,,entity.getRelationship()="+entity.getRelationship());
//        }
//        if(!(etv_date.getEditText()+" "+etv_date.getEditTextRight()).equals(entity.getDate())){
//            LogUtil.i(TAG,"etv_date.getEditText()="+(etv_date.getEditText()+" "+etv_date.getEditTextRight())+",,,entity.getDate()="+entity.getDate());
//        }
//        if(!TextUtils.isEmpty(etv_remark.getEditText())&&!etv_remark.getEditText().equals(entity.getRemark())){
//            LogUtil.i(TAG,"etv_remark.getEditText()="+etv_remark.getEditText()+",,,entity.getRemark()="+entity.getRemark());
//        }

        if(entity!=null&&(!etv_content.getEditText().equals(entity.getContent())||!etv_person.getEditText().equals(entity.getPerson())||!etvRelationShip.getEditText().equals(entity.getRelationship())
                ||!(etv_date.getEditText()+" "+etv_date.getEditTextRight()).equals(entity.getDate())
                ||!TextUtils.isEmpty(etv_remark.getEditText())&&!etv_remark.getEditText().equals(entity.getRemark()))){
           return true;
        }else{
           return etv_content.isEdited||etv_person.isEdited||etvRelationShip.isEdited||etv_date.isEdited;
        }
    }



    public void onResume() {
        super.onResume();
        StatService.onPageStart(this, "纪念日编辑");
    }

    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this, "纪念日编辑");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            boolean isChanged = etv_content.isEdited || etv_person.isEdited || etvRelationShip.isEdited;
//            LogUtil.i(TAG, "onKeyDown:isChanged=" + isChanged);
//            LogUtil.i(TAG, "onKeyDown:AppUtils.backToast(AnniversaryEditActivity.this,isChanged)=" + AppUtils.backToast(AnniversaryEditActivity.this, isChanged));
//            if (!AppUtils.backToast(AnniversaryEditActivity.this, isChanged)) {
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }

    @Override
    protected void onBack() {
        isPreservable=checkUpdate();
        LogUtil.i(TAG,"isPreservable="+isPreservable);
        super.onBack();
    }
}
