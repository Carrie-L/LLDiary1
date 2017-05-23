package com.carrie.lldiary.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.base.BaseActivity;
import com.carrie.lldiary.base.BaseHandler;
import com.carrie.lldiary.dao.Diary;
import com.carrie.lldiary.entity.DiaryBg;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.DialogIconView;
import com.carrie.lldiary.view.DialogRecyclerView;
import com.carrie.lldiary.view.TitleView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.id.list;
import static com.carrie.lldiary.R.drawable.back;
import static com.carrie.lldiary.ui.DiaryListActivity.BUNDLE;
import static com.carrie.lldiary.ui.DiaryListActivity.REQUEST_ADD;
import static com.carrie.lldiary.ui.DiaryListActivity.REQUEST_EDIT;
import static com.carrie.lldiary.utils.AppUtils.backToast;


/**
 * Created by new on 2017/4/14.
 * 正宗的日记编辑页
 */

public class DiaryActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "DiaryActivity";
    private final static int EDIT = 0;
    private final static int SAVE = 1;
    @BindView(R.id.diary_container)
    protected CoordinatorLayout mContainer;
    @BindView(R.id.tv_date)
    protected TextView tvDate;
    @BindView(R.id.iv_weather)
    protected ImageView ivWeather;
    @BindView(R.id.iv_mood)
    protected ImageView ivMood;
    @BindView(R.id.btn_bg)
    protected ImageView btnBG;
    @BindView(R.id.btn_size)
    protected ImageView ivTextSize;
    @BindView(R.id.et_diary_title)
    protected MaterialEditText metTitle;
    @BindView(R.id.et_diary)
    protected EditText etDiary;
    @BindView(R.id.title_view_diary)
    protected TitleView mTitleView;
    @BindView(R.id.app_bar)
    protected AppBarLayout mAppBarLayout;
    @BindView(R.id.ll_header)
    protected LinearLayout mLlHeader;

    private DialogIconView iconView;
    private ArrayList<DiaryBg> bgs;
    private PopupWindow popupWindow;
    private View popupView;
    private TextView tvEdit;
    private TextView tvShare;
    private boolean isEditable = true;
    private String mBgName;
    private Integer mTextColor = 0;
    private Integer mTextSize = 14;
    private String mLabel;
    private String mWeatherIcon = "drawable|weather_1";
    private String mMoodIcon = "drawable|emoji_1";
    private long id = 0;
    private String mCurrDate;
    private Diary diary;
    private View mTextSizeView;
    private TextView tvSmall;
    private TextView tvMiddle;
    private TextView tvLarge;
    private TextView tvXLarge;
    private TextView tvXXLarge;
    private PopupWindow mSizePopup;
    private ArrayList<DiaryBg> mWeatherIcons;
    private ArrayList<DiaryBg> mMoodIcons;

    @Override
    protected void initPre() {
        mLayoutId = R.layout.activity_diary4;
        mShowTitleView = false;
    }

    private void onRightClick() {
        LogUtil.i(TAG, "isEditable=" + isEditable);
        if (isEditable) {//编辑模式的保存
            if (!TextUtils.isEmpty(metTitle.getText().toString().trim()) || !TextUtils.isEmpty(etDiary.getText().toString().trim())) {
                save();
            }
        } else {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupDismiss();
            } else
                showPopupWindow();
        }
    }

    @Override
    protected void initData() {
        init();
        setEditableTitle();
        mLabel = getString(R.string.diary);
        Intent gIntent = getIntent();
        if (gIntent != null) {
            diary = getIntent().getParcelableExtra("Diary");
            if (diary != null) {
                setSavedTitle();
                setEditable(false);
                id = diary.getId();
                showDiary();
            }
        }
        metTitle.setSelection(metTitle.getText().length());
        etDiary.setSelection(etDiary.getText().length());
        setTitleViewClick();
    }

    private void setTitleViewClick() {
        mTitleView.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick();
            }
        });
        mTitleView.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void showDiary() {
        tvDate.setText(diary.getCreateDate());
        mBgName = diary.getBg();
        mTextColor = diary.getTextColor();
        mTextSize = diary.getTextSize();
        if (diary.getTextSize() != null) {
            mTextSize = diary.getTextSize();
        }
        LogUtil.i(TAG, "showDiary_label 0 =" + mLabel);
        mLabel = diary.getLabel();
        LogUtil.i(TAG, "showDiary_label 1 =" + mLabel);
        mWeatherIcon = diary.getWeather();
        mMoodIcon = diary.getMood();
        setIcon(mWeatherIcon, ivWeather);
        setIcon(mMoodIcon, ivMood);
        setBackground();
        setTextColor();
        metTitle.setText(diary.getTitle());
        etDiary.setText(diary.getContent());
        etDiary.setTextSize(mTextSize);
        LogUtil.i(TAG, "showDiary_diary=" + diary.toString());
    }

    private void showPopupWindow() {
        if (popupView == null) {
            popupView = LayoutInflater.from(this).inflate(R.layout.popup_diary_view, null);
            tvEdit = (TextView) popupView.findViewById(R.id.popup_edit);
            tvShare = (TextView) popupView.findViewById(R.id.popup_share);
        }
        popupWindow = new PopupWindow(popupView, AppUtils.getScreenWidth() / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAsDropDown(mTitleView, AppUtils.getScreenWidth(), 0);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditable(true);
            }
        });
        popupView.findViewById(R.id.popup_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }

    // TODO: 2017/4/16
    private void share() {
        Toast.makeText(getApplicationContext(), "分享日记", Toast.LENGTH_SHORT).show();
        popupDismiss();
    }

    private void save() {
        mCurrDate = DateUtil.getCurrent_Date_Time_Week();
        LogUtil.i(TAG, "save_id=" + id);
        LogUtil.i(TAG, "save_label=" + mLabel);
        if (id > 0) {
            update();
        } else {
            insert();
        }
        AppUtils.closeSoftKb(this);
        mHandler.sendEmptyMessage(SAVE);
        setEditable(false);
        Snackbar.make(mContainer, getString(R.string.save_success), Snackbar.LENGTH_LONG).setAction(getString(R.string.label), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryActivity.this, LabelAddActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(BUNDLE, diary);
                startActivityForResult(intent, REQUEST_EDIT);
            }
        }).show();
    }

    private void insert() {
        diary = new Diary(null, metTitle.getText().toString().trim(), etDiary.getText().toString().trim(), mCurrDate, mCurrDate, mWeatherIcon, mMoodIcon, TextUtils.isEmpty(mBgName) ? "0" : mBgName, mTextSize, mTextColor, mLabel);
        id = App.dbHelper.insertDiary(diary);
        LogUtil.i(TAG, "insert_id=" + id);
        setResult();
    }

    private void update() {
        LogUtil.i(TAG, "update_id=" + id);
        diary.setTitle(metTitle.getText().toString().trim());
        diary.setContent(etDiary.getText().toString().trim());
        diary.setUpdateDate(mCurrDate);
        diary.setWeather(mWeatherIcon);
        diary.setMood(mMoodIcon);
        diary.setBg(mBgName);
        diary.setTextSize(mTextSize);
        diary.setTextColor(mTextColor);
        diary.setLabel(mLabel);
        App.dbHelper.updateDiary(diary);
        setResult();
    }

    private void setResult() {
        Intent data = new Intent();
        data.putExtra(BUNDLE, diary);
        setResult(RESULT_OK, data);
    }

    DiaryHandler mHandler = new DiaryHandler(this);

    private static class DiaryHandler extends BaseHandler<DiaryActivity> {

        private DiaryHandler(DiaryActivity activity) {
            super(activity);
        }

        @Override
        public void handleMsg(Message msg, DiaryActivity diaryActivity) {
            switch (msg.what) {
                case SAVE:
                    diaryActivity.setSavedTitle();
                    break;
                case EDIT:
                    diaryActivity.setEditableTitle();
                    diaryActivity.metTitle.setCursorVisible(true);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_weather:
                selectWeather();
                dismissSizePopupWindow();
                break;
            case R.id.iv_mood:
                selectMood();
                dismissSizePopupWindow();
                break;
            case R.id.btn_bg:
                selectBackground();
                dismissSizePopupWindow();
                break;
            case R.id.btn_size:
                setTextSize();
                break;
        }
    }

    private void selectWeather() {
        setWeatherIcons();
        final DialogRecyclerView dialogView = new DialogRecyclerView(this);
        dialogView.setLayoutManager(new GridLayoutManager(this,5));
        dialogView.setTitle(R.string.diary_select_weather);
        dialogView.setList(mWeatherIcons);
        dialogView.setGridAdapter();
        dialogView.alertDialog();
        dialogView.setOnItemClickListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mWeatherIcon = mWeatherIcons.get(position).bgName;
                setIcon(mWeatherIcon, ivWeather);
                dialogView.cancelDialog();
            }
        });
    }

    private void selectMood() {
        final DialogRecyclerView dialogView = new DialogRecyclerView(this);
        setMoodIcons();
        dialogView.setTitle(R.string.diary_select_mood);
        dialogView.setList(mMoodIcons);
        dialogView.setGridAdapter();
        dialogView.alertDialog();
        dialogView.setOnItemClickListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mMoodIcon = mMoodIcons.get(position).bgName;
                setIcon(mMoodIcon, ivMood);
                dialogView.cancelDialog();
            }
        });
    }

    private void selectBackground() {
        LogUtil.e(TAG, "selectBackground");
        final DialogRecyclerView dialogView = new DialogRecyclerView(this);
        setDialogBgs();
        dialogView.setList(bgs);
        dialogView.setGridAdapter();
        dialogView.alertDialog();
        dialogView.setOnItemClickListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mTextColor = bgs.get(position).textColor;
                mBgName = bgs.get(position).bgName;
                setBackground();
                setTextColor();
                dialogView.cancelDialog();
            }
        });
    }

    private void setTextSize() {
        if (mSizePopup != null && mSizePopup.isShowing()) {
            dismissSizePopupWindow();
        } else {
            showSizePopupWindow();
        }
        tvSmall.setOnClickListener(new OnSizeClickListener());
        tvMiddle.setOnClickListener(new OnSizeClickListener());
        tvLarge.setOnClickListener(new OnSizeClickListener());
        tvXLarge.setOnClickListener(new OnSizeClickListener());
        tvXXLarge.setOnClickListener(new OnSizeClickListener());
    }

    private class OnSizeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_size_small:
                    mTextSize = 12;
                    etDiary.setTextSize(12);
                    break;
                case R.id.tv_size_middle:
                    mTextSize = 14;
                    etDiary.setTextSize(14);
                    break;
                case R.id.tv_size_large:
                    mTextSize = 16;
                    etDiary.setTextSize(16);
                    break;
                case R.id.tv_size_xlarge:
                    mTextSize = 20;
                    etDiary.setTextSize(20);
                    break;
                case R.id.tv_size_xxlarge:
                    mTextSize = 24;
                    etDiary.setTextSize(24);
                    break;
            }
        }
    }

    private void showSizePopupWindow() {
        if (mTextSizeView == null) {
            mTextSizeView = LayoutInflater.from(this).inflate(R.layout.view_set_textsize, null);
            tvSmall = (TextView) mTextSizeView.findViewById(R.id.tv_size_small);
            tvMiddle = (TextView) mTextSizeView.findViewById(R.id.tv_size_middle);
            tvLarge = (TextView) mTextSizeView.findViewById(R.id.tv_size_large);
            tvXLarge = (TextView) mTextSizeView.findViewById(R.id.tv_size_xlarge);
            tvXXLarge = (TextView) mTextSizeView.findViewById(R.id.tv_size_xxlarge);
        }
        mSizePopup = new PopupWindow(mTextSizeView, AppUtils.getScreenWidth() / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSizePopup.showAsDropDown(mLlHeader, AppUtils.getScreenWidth(), 0);
    }

    private void dismissSizePopupWindow() {
        if (mSizePopup != null && mSizePopup.isShowing()) {
            mSizePopup.dismiss();
            mSizePopup = null;
        }
    }

    /**
     * 不可编辑状态下的标题栏
     */
    private void setSavedTitle() {
        mTitleView.setRightIcon(R.drawable.ic_vertical_dot);
        mTitleView.setTitle(getString(R.string.my_diary));
        metTitle.setCursorVisible(false);
    }

    /**
     * 可编辑状态下的标题栏
     */
    private void setEditableTitle() {
        mTitleView.setRightIcon(R.drawable.ok_normal);
        mTitleView.setTitle(getString(R.string.edit_diary));
    }

    private void setTextColor() {
        if (mTextColor != null) {
            metTitle.setTextColor(mTextColor == 1 ? Color.WHITE : Color.BLACK);
            etDiary.setTextColor(mTextColor == 1 ? Color.WHITE : getResources().getColor(R.color.primary_text_color_trans));
        }
    }

    private void setBackground() {
        if (!TextUtils.isEmpty(mBgName) && mBgName.contains("|")) {
            if (mBgName.contains("bg_none")) {
                mContainer.setBackground(null);
            } else {
                int id = getResources().getIdentifier(mBgName.split("\\|")[1], mBgName.split("\\|")[0], getPackageName());
                mContainer.setBackground(getResources().getDrawable(id));
                mContainer.getBackground().setAlpha(191);//设置不透明度
            }
        }
    }

    private void setIcon(String name, ImageView imageView) {
        if (!TextUtils.isEmpty(name) && name.contains("|")) {
            int id = getResources().getIdentifier(name.split("\\|")[1], name.split("\\|")[0], getPackageName());
            imageView.setBackground(getResources().getDrawable(id));
        }
    }

    private void setWeatherIcons() {
        String[] names = new String[]{"drawable|weather_1", "drawable|weather_2", "drawable|weather_3", "drawable|weather_4", "drawable|weather_5", "drawable|weather_6",
                "drawable|weather_7", "drawable|weather_8", "drawable|weather_9", "drawable|weather_10", "drawable|weather_11", "drawable|weather_12"};
        mWeatherIcons = new ArrayList<>();
        DiaryBg diaryBg;
        int length = names.length;
        for (int i = 0; i < length; i++) {
            diaryBg = new DiaryBg(names[i], 0);
            mWeatherIcons.add(diaryBg);
        }
    }

    private void setMoodIcons() {
        String[] names = new String[]{"drawable|emoji_1", "drawable|emoji_2", "drawable|emoji_3", "drawable|emoji_4", "drawable|emoji_5", "drawable|emoji_6",
                "drawable|emoji_7", "drawable|emoji_8", "drawable|emoji_9", "drawable|emoji_10", "drawable|emoji_11", "drawable|emoji_12"
        };
        mMoodIcons = new ArrayList<>();
        DiaryBg diaryBg;
        int length = names.length;
        for (int i = 0; i < length; i++) {
            diaryBg = new DiaryBg(names[i], 0);
            mMoodIcons.add(diaryBg);
        }
    }


    private void setDialogBgs() {
        //深色的：最好用700稀释
        String[] names = new String[]{"drawable|bg_none", "color|fruit_green", "color|pink", "color|bg_pure_seashell",
                "color|bg_pure_honeydew", "color|bg_pure_corn_silk", "color|bg_pure_primary_color",
                "color|light_green_accent", "color|indigo_700", "color|teal_primary_color",
                "color|cyan_accent", "color|pink_pink", "color|colorAccent",
                "drawable|bg_pic_1", "drawable|bg_pic_2", "drawable|bg_pic_3", "drawable|bg_pic_4", "drawable|bg_pic_5"
        };
        Integer[] textColors = new Integer[]{0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0};
        bgs = new ArrayList<>();
        DiaryBg diaryBg;
        int length = names.length;
        for (int i = 0; i < length; i++) {
            diaryBg = new DiaryBg(names[i], textColors[i]);
            bgs.add(diaryBg);
        }
    }

    private void init() {
        ButterKnife.bind(this);
        tvDate.setText(DateUtil.getCurrent_Date_Time_Week());
        ivWeather.setOnClickListener(this);
        ivMood.setOnClickListener(this);
        btnBG.setOnClickListener(this);
        ivTextSize.setOnClickListener(this);
        iconView = new DialogIconView(this);
        etDiary.setLineSpacing(8f, 1.3f);
        etDiary.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {//回车监听。如果return true，则为忽视回车。
                    etDiary.append("\n");
                }
                return false;
            }
        });
        etDiary.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isEditable) {
                    mAppBarLayout.setExpanded(false);
                    dismissSizePopupWindow();
                }
                return false;
            }
        });
//        metTitle.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                LogUtil.i(TAG, "start=" + start + ",after=" + after + ",count=" + count);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                LogUtil.i(TAG, "start=" + start + ",before=" + before + ",count=" + count);
//                if (!isEditable && !TextUtils.isEmpty(s.toString().trim()) && start != 0) {
//                    LogUtil.i(TAG, "哈哈 现在要加标题了吧");
//                    isEditable = true;
//                    mHandler.sendEmptyMessage(EDIT);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
    }

    private void openEdit() {
        Toast.makeText(getApplicationContext(), getString(R.string.diary_open_edit), Toast.LENGTH_SHORT).show();
        etDiary.setFocusableInTouchMode(true);
        etDiary.setFocusable(true);
        etDiary.requestFocus();
        metTitle.setFocusableInTouchMode(true);
        metTitle.setFocusable(true);
        metTitle.requestFocus();
        metTitle.setCursorVisible(true);
        isEditable = true;
        setEditableTitle();
        popupDismiss();
        //显示小按钮
        setLittleIconVisibility(View.VISIBLE);
        setLittleIconClickable(true);
    }

    private void closeEdit() {
        etDiary.setFocusable(false);
        etDiary.setFocusableInTouchMode(false);
        metTitle.setFocusable(false);
        metTitle.setFocusableInTouchMode(false);
        setLittleIconVisibility(View.GONE);
        setLittleIconClickable(false);
    }

    private void setEditable(boolean editable) {
        isEditable = editable;
        isPreservable = editable;
        if (editable) {
            openEdit();
        } else {
            closeEdit();
            dismissSizePopupWindow();
        }
    }

    private void popupDismiss() {
        popupWindow.dismiss();
        popupWindow = null;
    }

    private void setLittleIconVisibility(int visibility) {
        btnBG.setVisibility(visibility);
        ivTextSize.setVisibility(visibility);
    }

    private void setLittleIconClickable(boolean clickable) {
        ivWeather.setClickable(clickable);
        ivMood.setClickable(clickable);
        btnBG.setClickable(clickable);
        ivTextSize.setClickable(clickable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_EDIT) {
                setResult(RESULT_OK, data);
            }
        }
    }

    @Override
    protected void onBack() {
//        super.onBack();

        back();

    }

    private void back() {
        if (TextUtils.isEmpty(metTitle.getText().toString().trim()) && TextUtils.isEmpty(etDiary.getText().toString().trim())) {
            isEditable = false;
        }
        //在编辑模式下离开，提示尚未保存
        boolean toast = AppUtils.backToast(this, isEditable);
        if (!toast) {
            finish();
        }
    }
}
